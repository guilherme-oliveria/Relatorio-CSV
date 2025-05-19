package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.endereco.Estado;
import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import br.jus.tjro.gabinete.model.gab.pessoa.PessoaDocumento;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte;
import br.jus.tjro.gabinete.model.gab.transiente.PessoaDocumentosTransiente;
import br.jus.tjro.gabinete.repository.gab.pessoa.PessoaDocumentoRepository;
import br.jus.tjro.gabinete.service.remoto.PessoaDocumentosRemotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class PessoaDocumentoService {

    private final PessoaDocumentoRepository pessoaDocumentoRepository;

    private final PessoaDocumentosRemotoService pessoaDocumentosRemotoService;

    private final PessoaService pessoaService;

    private final EstadoService estadoService;

    @Autowired
    public PessoaDocumentoService(
        PessoaDocumentosRemotoService pessoaDocumentosRemotoService,
        PessoaDocumentoRepository pessoaDocumentoRepository,
        PessoaService pessoaService,
        EstadoService estadoService){
        this.pessoaDocumentosRemotoService = pessoaDocumentosRemotoService;
        this.pessoaDocumentoRepository = pessoaDocumentoRepository;
        this.pessoaService = pessoaService;
        this.estadoService = estadoService;
    }

    public PessoaDocumento save(PessoaDocumento pessoaDocumento) {
        return pessoaDocumentoRepository.save(pessoaDocumento);
    }

    public void excluir(List<PessoaDocumento> pessoaDocumentos) {
        pessoaDocumentoRepository.deleteAll(pessoaDocumentos);
    }

    public List<PessoaDocumento> save(List<PessoaDocumento> pessoaDocumentos) {
        return pessoaDocumentoRepository.saveAll(pessoaDocumentos);
    }

    public List<PessoaDocumento> importaOuAtualizaDocumentosDaPessoa(ProcessoParte processoParte, FonteDadosEnum fonte) throws Exception {
        Long idPessoaLegado = processoParte.getPessoa().getIdPessoaLegado();
        List<PessoaDocumento> documentosParaSalvar = new ArrayList<>();
        List<PessoaDocumentosTransiente> listaDeDocumentosRemotos =
            Arrays.asList(pessoaDocumentosRemotoService.buscaDocumentosDaPessoa(idPessoaLegado, fonte));
        List<PessoaDocumento> listaDeDocumentosLocais = processoParte.getPessoa().getPessoaDocumentos();

        // Procura e exclui documentos locais que não estão mais no pje
        List<Long> idsLegados = listaDeDocumentosRemotos.stream().map(PessoaDocumentosTransiente::getIdPessoaDocIdentificacao).collect(Collectors.toList());
        List<PessoaDocumento> documentosParaExcluir = listaDeDocumentosLocais.stream().filter(pdl -> !idsLegados.contains(pdl.getIdDocumentoLegado())).collect(Collectors.toList());
        // Procura e exclui documentos locais de outras partes com mesmo idLegado
        List<Long> idsExcluir = documentosParaExcluir.stream().map(PessoaDocumento::getIdDocumentoLegado).collect(Collectors.toList());

        if(documentosParaExcluir.size() > 0) {
            excluir(documentosParaExcluir);
            listaDeDocumentosLocais = listaDeDocumentosLocais.stream()
                .filter(pessoaDocumento -> !idsExcluir.contains(pessoaDocumento.getIdDocumentoLegado()))
                .collect(Collectors.toList());
        }

        // Adiciona os novos
        List<PessoaDocumento> finalListaDeDocumentosLocais = listaDeDocumentosLocais;
        listaDeDocumentosRemotos.stream().forEach(remoto -> {
            PessoaDocumento local = finalListaDeDocumentosLocais.stream()
                .filter(l -> l.getIdDocumentoLegado().equals(remoto.getIdPessoaDocIdentificacao()))
                .findFirst().orElse(null);

            if(local == null) {
                local = new PessoaDocumento(
                    remoto.getIdPessoaDocIdentificacao(), remoto.getIdPessoaLegado(),
                    pessoaService.buscaPorIdLegado(idPessoaLegado,fonte),
                    remoto.getTipoDocumento(), remoto.getNrDocumento(),
                    estadoService.busca(remoto.getEstado())
                );
                documentosParaSalvar.add(local);
            } else {
                if(
                    !Objects.equals(remoto.getNrDocumento(), local.getTipoDocumento()) ||
                    !Objects.equals(remoto.getNrDocumento(), local.getDocumento()) ||
                    (remoto.getEstado() == null && local.getEstado() != null) ||
                    (remoto.getEstado() != null && local.getEstado() != null && (remoto.getEstado().getDescricao().equals(local.getEstado().getDescricao())))
                ){
                    Estado estado = estadoService.busca(remoto.getEstado());
                    local.setEstado(estado);
                    local.setDocumento(remoto.getNrDocumento());
                    local.setTipoDocumento(remoto.getTipoDocumento());
                    documentosParaSalvar.add(local);
                }
            }
        });

        save(documentosParaSalvar);

        return documentosParaSalvar;
    }

    public List<PessoaDocumento> buscaDocumentosDaPessoa(Pessoa pessoa) {
        List<PessoaDocumento> documentos = pessoaDocumentoRepository.findByPessoa(pessoa);
        return documentos == null ? new ArrayList<>() : documentos;
    }

    public List<PessoaDocumento> buscaDocumentosDaPessoaPorTipo(Pessoa pessoa, String tipoDeDoc) {
        return pessoaDocumentoRepository.findByPessoaAndTipoDocumento(pessoa, tipoDeDoc);
    }

    List<PessoaDocumento> findAdvogadoDocumentosPorProcesso(Processo processo){
        return pessoaDocumentoRepository.findAdvogadoDocumentosPorProcesso(processo);
    }
}
