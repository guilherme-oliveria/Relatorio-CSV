package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.exceptions.service.local.ProcessoParteServiceException;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.enums.TipoPessoaEnum;
import br.jus.tjro.gabinete.model.gab.enums.TipoPolo;
import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParteEndereco;
import br.jus.tjro.gabinete.model.gab.transiente.Partes;
import br.jus.tjro.gabinete.repository.gab.pessoa.PessoaRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ParteEnderecoRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoParteRepository;
import br.jus.tjro.gabinete.service.importacao.ObtemItensParaAdicionarAtualizarOuExcluir;
import br.jus.tjro.gabinete.service.remoto.ParteRemotoService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class ProcessoParteService {

    @Autowired
    private ParteEnderecoRepository parteEnderecoRepository;

    private final ProcessoParteRepository processoParteRepository;
    private final PessoaRepository pessoaRepository;
    private final ParteRemotoService parteRemotoService;


    private final Logger logger = LoggerFactory.getLogger(ProcessoParteService.class);

    @Autowired
    public ProcessoParteService(ParteRemotoService parteRemotoService,
                                ProcessoParteRepository processoParteRepository,
                                PessoaRepository pessoaRepository){
        this.parteRemotoService = parteRemotoService;
        this.processoParteRepository = processoParteRepository;
        this.pessoaRepository = pessoaRepository;
    }

    public ProcessoParte findOne(Long idParte) {
        return this.processoParteRepository.findById(idParte).orElseThrow(NullPointerException::new); // ToDo tratar a falta desse recurso
    }

    public ProcessoParte save(ProcessoParte processoParte) {
        try {
            return processoParteRepository.save(processoParte);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    public List<Partes> buscaParteProcessoPorIdProcessoEPolo(TipoPolo polo, Long idProcesso) {
        return processoParteRepository.buscaParteProcessoPorIdProcessoEPolo(polo, idProcesso);
    }

    public List<ProcessoParte> retornaTodasAsPartesPorProcesso(Processo processo) {
        List<ProcessoParte> partes = processoParteRepository.findByProcesso(processo);
        if (partes == null) return new ArrayList<>();
        partes = partes.stream().filter(p -> p.getTipoParte() != null && p.getPessoa() != null).collect(Collectors.toList());
        this.setSegredoDeJusticaNasPartes(partes, processo);
        return partes;
    }

    private void setSegredoDeJusticaNasPartes(List<ProcessoParte> partes, Processo processo) {
        partes.forEach(p -> {
            if (!p.getTipoParte().equals("ADVOGADO")) {
                p.setSobSegredo(processo.isSegredoJustica() || TipoPessoaEnum.A.equals(p.getPessoa().getTipoPessoa()));
            }
        });
    }

    public List<ProcessoParte> retornaTodasAsPartesLocais() {
        return Lists.newArrayList(processoParteRepository.findAll());
    }



    public Page<ProcessoParteEndereco> buscaEnderecosParte(Long idProcessoParte, Pageable pageable) {
        return parteEnderecoRepository.findByProcessoParte(new ProcessoParte(idProcessoParte), pageable);
    }

    public ProcessoParte montaObjProcessoParte(Long idParteLegado, String tipoParte, String procuradoria,
                                               TipoPolo tipoPolo, Processo processo, Pessoa pessoa) {

        return new ProcessoParte(
            idParteLegado, tipoParte, procuradoria, tipoPolo, processo, pessoa);
    }

//    @Transactional
    public void importaOuAtualizaPartesProcesso(Processo processo) throws Exception {
        Long idLegado = processo.getIdProcessoSistemaLegado();
        FonteDadosEnum fonte = processo.getSistema();
        List<Partes> partesRemoto;
        try{
            partesRemoto =  parteRemotoService.pegarPartesEInteressadosProcessoPorIdProcesso(idLegado, fonte);
        }catch (HttpServerErrorException e){
            throw new Exception("Erro ao buscar partes no servidor remoto! O servidor diz: "+e.getResponseBodyAsString(),e);
        }

        List<Long> idsPessoaRemoto = new ArrayList();
        List<Pessoa> pessoaRemoto = new ArrayList();

        partesRemoto.forEach(p-> {
            idsPessoaRemoto.add(p.getPessoa().getId());
            pessoaRemoto.add(p.getPessoa());
        });

        List<ProcessoParte> partesLocais = processoParteRepository.findByProcesso(processo);
        ObtemItensParaAdicionarAtualizarOuExcluir partesAddOuExcluir = new ObtemItensParaAdicionarAtualizarOuExcluir(partesLocais,partesRemoto);
        this.deleteCascade(partesAddOuExcluir.getListaExclusao());
        List<ProcessoParte> atualizar = new ArrayList<>();

        partesAddOuExcluir.getListaAtualizacao().forEach((o1, o2) -> {
            ProcessoParte local = (ProcessoParte) o1;
            Partes remoto = (Partes) o2;
            local.setPessoa(buscaOuCriaPessoa(remoto, fonte));
            local.setProcuradoria(remoto.getProcuradoria());
            local.setTipoParte(remoto.getTipoParte());
            local.setTipoPolo(remoto.getTipoPolo());
            atualizar.add(local);
        });

        try{
            partesAddOuExcluir.getListaAdicao().forEach(o -> {
                Partes remoto = (Partes) o;
                Pessoa pessoa = buscaOuCriaPessoa(remoto, fonte);
                ProcessoParte local = montaObjProcessoParte(remoto.getId(), remoto.getTipoParte(), remoto.getProcuradoria(),remoto.getTipoPolo(), processo, pessoa);
                processoParteRepository.save(local);
            });

            if(atualizar.size() > 0)
                processoParteRepository.saveAll(atualizar);
        }catch (Exception e){
            throw new ProcessoParteServiceException("Erro ao salvar partes. ID Processo: "+processo.getId()+" "+processo.getNumeroProcesso(),e);
        }
    }

    private Pessoa buscaOuCriaPessoa(Partes remoto, FonteDadosEnum fonte) {
        List<Long> ids = List.of((long)remoto.getIdPessoaLegado());
        Pessoa pessoa = pessoaRepository.findByIdPessoaLegadoInAndSistema(ids,fonte).stream().findFirst().orElse(null);
        if(pessoa == null) {
            pessoa = pessoaRepository.save(new Pessoa(
                (long) remoto.getIdPessoaLegado(),
                remoto.getNome(),
                remoto.getEmail(),
                remoto.getPessoa().getDataNascimento(),
                remoto.getPessoa().getDataObito(),
                remoto.getPessoa().getTipoPessoa(),
                fonte));
        }
        return pessoa;
    }

    private void deleteCascade(List<ProcessoParte> listaExclusao) {
        listaExclusao.forEach(e -> parteEnderecoRepository.deleteAll(e.getProcessoParteEnderecos()));
        processoParteRepository.deleteAll(listaExclusao);
    }

    public List<Pessoa> removePessoasQueJaExistemEmListaDePartes(List<ProcessoParte> partesParaAtualizar){
        List<Pessoa> pessoasParaSalvar = new ArrayList<>();
        partesParaAtualizar.stream().forEach(p -> {
            Pessoa pessoaJaExiste = pessoasParaSalvar.stream().filter(x -> x.getIdPessoaLegado().equals(p.getPessoa().getIdPessoaLegado())).findFirst().orElse(null);
            if(p.getPessoa().getId() == null && pessoaJaExiste == null)
                pessoasParaSalvar.add(p.getPessoa());
        });
        return pessoasParaSalvar;
    }

    public List<ProcessoParte> verificaQuaisPartesDevemSerAtualizadas(List<Pessoa> pessoasLocais, List<ProcessoParte> partesLocais, List<Partes> partesRemoto, Processo processo){
        List<ProcessoParte> partesParaAtualizar = new ArrayList<>();
        Integer partesRemotosCount = partesRemoto.size();
        Integer partesLocaisCount = partesLocais.size();
        if (partesLocaisCount != 0) {

            Set<Long> idsLocais = partesLocais.stream().map((ProcessoParte::getIdParteLegado))
                .collect(Collectors.toSet());

            // atualiza polo ou tipo da parte já cadastrada
            if (partesLocaisCount > 0) {
                for (Partes parte : partesRemoto) {

                    List<ProcessoParte> getParteJaNoBanco = partesLocais.stream().filter(
                        parteLocal -> parteLocal.getIdParteLegado().equals(parte.getId())
                    ).collect(Collectors.toList());
                    if (getParteJaNoBanco.size() > 0) {
                        ProcessoParte primeiraDaLista = getParteJaNoBanco.get(0);
                        if (!primeiraDaLista.getTipoPolo().equals(parte.getTipoPolo()) ||
                            !primeiraDaLista.getTipoParte().equals(parte.getTipoParte())) {
                            primeiraDaLista.setTipoPolo(parte.getTipoPolo());
                            primeiraDaLista.setTipoParte(parte.getTipoParte());
                            partesParaAtualizar.add(primeiraDaLista);
                        }
                    }
                }
            }

            if (partesRemotosCount > partesLocaisCount) {
                // remove da lista de documentos remotos os já existentes localmente
                partesRemoto = partesRemoto.stream()
                    .filter(docR -> !idsLocais.contains(docR.getId())).collect(Collectors.toList());
            }

        }

        if (!Objects.equals(partesRemotosCount, partesLocaisCount)) {
            for (Partes parte : partesRemoto) {
                Pessoa pessoa = new Pessoa((long) parte.getIdPessoaLegado(), parte.getNome(),
                    parte.getEmail(), parte.getPessoa().getDataNascimento(),
                    parte.getPessoa().getDataObito(), parte.getPessoa().getTipoPessoa(),processo.getFonteDados());

                ProcessoParte processoParte = montaObjProcessoParte(
                    parte.getId(), parte.getTipoParte(), parte.getProcuradoria(), parte.getTipoPolo(), processo, pessoa);

                partesParaAtualizar.add(processoParte);
            }

            if(pessoasLocais != null) {
                for (Pessoa pessoa : pessoasLocais) {
                    partesParaAtualizar.stream()
                        .filter(p -> p.getPessoa().getIdPessoaLegado().equals(pessoa.getIdPessoaLegado()))
                        .forEach(processoParte -> processoParte.setPessoa(pessoa));
                }
            }
        }

        partesParaAtualizar.forEach(p -> {
            if(pessoasLocais != null && p.getPessoa().getId() == null) {
                Pessoa pl = pessoasLocais.stream().filter(l -> l.getIdPessoaLegado().equals(p.getPessoa().getIdPessoaLegado())).findFirst().orElse(null);
                if(pl != null)
                    p.setPessoa(pl);
            }
        });

        return partesParaAtualizar;
    }
}
