package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.interfaces.ModeloDocumentoService;
import br.jus.tjro.gabinete.model.gab.enums.TipoAssinatura;
import br.jus.tjro.gabinete.model.gab.minuta.*;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaMovimento;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaMovimentoComplemento;
import br.jus.tjro.gabinete.model.gab.tpu.TpuMovimento;
import br.jus.tjro.gabinete.model.gab.transiente.ContainerMinutaSemelhancaIA;
import br.jus.tjro.gabinete.model.gab.transiente.MinutaEmLote;
import br.jus.tjro.gabinete.model.gab.transiente.ModeloMinutaCabecalhoRodape;
import br.jus.tjro.gabinete.model.gab.transiente.ProcessoConcluso;
import br.jus.tjro.gabinete.repository.gab.minuta.MinutaMovimentoRepository;
import br.jus.tjro.gabinete.repository.gab.minuta.MinutasRepository;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.service.local.tpu.TpuMovimentoService;
import br.jus.tjro.gabinete.service.remoto.ia.IaClassificacaoMinutaRemotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
public class MinutaService {
    private final Logger looger = LoggerFactory.getLogger(MinutaService.class);
    private final ModeloDocumentoService modeloDocumento;
    private final MinutasRepository minutasRepository;
    private final MinutaMovimentoRepository minutaMovimentoRepository;
    private final MinutaVersaoService minutaVersaoService;
    private final TpuMovimentoService tpuMovimentoService;
    private final PublicarProcessoDjeService publicarProcessoDjeService;
    private final ProcessoService processoService;
    private final ModeloMinutaService modeloMinutaService;
    private final MinutaAnexoService minutaAnexoService;

    @Autowired
    private AuthenticationUsuarioService auth;

    @Autowired
    private final ProcessosRepository processosRepository;

    @Autowired
    private IaClassificacaoMinutaRemotoService iaClassificacaoMinutaRemotoService;

    @Autowired
    private MinutaTarefaLogService minutaTarefaLogService;

    @Autowired
    public MinutaService(MinutasRepository minutasRepository,
                         MinutaMovimentoRepository minutaMovimentoRepository,
                         MinutaVersaoService minutaVersaoService,
                         TpuMovimentoService tpuMovimentoService,
                         PublicarProcessoDjeService publicarProcessoDjeService,
                         ProcessoService processoService,
                         ProcessosRepository processosRepository,
                         ModeloMinutaService modeloMinutaService,
                         ModeloDocumentoService modeloDocumento,
                         MinutaAnexoService minutaAnexoService) {
        this.minutasRepository = minutasRepository;
        this.minutaMovimentoRepository = minutaMovimentoRepository;
        this.minutaVersaoService = minutaVersaoService;
        this.tpuMovimentoService = tpuMovimentoService;
        this.publicarProcessoDjeService = publicarProcessoDjeService;
        this.processosRepository = processosRepository;
        this.modeloMinutaService = modeloMinutaService;
        this.processoService = processoService;
        this.modeloDocumento = modeloDocumento;
        this.minutaAnexoService = minutaAnexoService;
    }

    @Transactional
    public Minuta salvarComVersao(Minuta minuta,Usuario usuario) throws Exception {
        verificaIntegridadeDoBancoAoSalvar(minuta);
        minuta = removeAtributosQueNaoDevemSerSalvos(minuta);
        minuta = verificaEhRemovePublicacaoDje(minuta);
        minuta = controleVersaoMinuta(minuta, usuario);
        minuta = controleTipoAssinatura(minuta);
        return save(minuta);
    }

    public ContainerMinutaSemelhancaIA pesquisarMinutasParaAssinarSimilares(String idOrgaoJulgador, Long idProcessoReferencia) throws Exception {
        List<Processo> processos = processoService.findByTarefaParaOrgaoJulgador(TarefaEnum.Assinar, idOrgaoJulgador);
        List<Minuta> minutasDosProcessos = processos.stream().map(p -> {
            try {
                return p.minutaEmElaboracao().get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        })/*.sorted()*/.collect(Collectors.toList());
        return iaClassificacaoMinutaRemotoService.pesquisarMinutasSimilares(minutasDosProcessos, idProcessoReferencia);
    }

    public Minuta save(Minuta minuta) {
        return minutasRepository.save(minuta);
    }

    private Minuta verificaEhRemovePublicacaoDje(Minuta minuta) {
        Minuta modified = publicarProcessoDjeService.verificaEhRemovePublicacaoDje(minuta);
        return modified;
    }

    private Minuta controleVersaoMinuta(Minuta minuta,Usuario usuario) throws Exception {
        Integer versao = minutaVersaoService.minutaVersaoComparaEhSalva(minuta, usuario);
        minuta.setVersao(versao);
        return minuta;
    }

    private Minuta controleTipoAssinatura(Minuta minuta) throws Exception {
        if(minuta.getAssinatura()!=null){
            minuta.setTipoAssinatura(TipoAssinatura.Detached);
        }else{
            minuta.setTipoAssinatura(TipoAssinatura.Cms);
        }
        return minuta;
    }

    public Minuta findOne(Long id) throws Exception {
        Minuta minuta = minutasRepository.findById(id).orElseThrow(NullPointerException::new);
        minuta.setMinutaMovimentos(getMinutaMovimentos(minuta));
        return minuta;
    }

    private List<MinutaMovimento> getMinutaMovimentos(Minuta minuta) {
        return minutaMovimentoRepository.getAllByMinuta(minuta);
    }

    @PreAuthorize("validaProcessoAcessado(#processo)")
    public Minuta pegaUltimaMinutaByProcesso(Processo processo) throws Exception {
        if(!processo.minutaEmElaboracao().isPresent())
            return criaMinutaVoid(processo,auth.getUsuario(SecurityContextHolder.getContext().getAuthentication()),null);
        Minuta minuta = processo.minutaEmElaboracao().get();
        setCabecalho(minuta);
        if (minuta.getAnexos() != null && minuta.getAnexos().size() > 0)
            MinutaAnexo.ordenaPorPosicao(minuta.getAnexos());
        return minuta;
    }

    public Minuta ordenaUltimaMinuta(Minuta minuta) throws Exception {
        setCabecalho(minuta);
        if (minuta.getAnexos() != null && minuta.getAnexos().size() > 0)
            MinutaAnexo.ordenaPorPosicao(minuta.getAnexos());
        return minuta;
    }

    public Minuta pegaUltimaMinutaByProcesso(Long idProcesso) throws Exception {
        Optional<Minuta> minuta = minutasRepository.getMinutaEmElaboracaoByIdProcesso(idProcesso);
        if (!minuta.isPresent()) {
            Optional<Processo> processo = processosRepository.findById(idProcesso);
            if (!processo.isPresent())
                throw new Exception("Não existe processo com o id. " + idProcesso);
            return pegaUltimaMinutaByProcesso(processo.get());
        }
        return ordenaUltimaMinuta(minuta.get());
    }

    public Minuta recuperaMinutaNaoAssinadoDoProcesso(Usuario usuario, Long idProcesso) throws Exception {
        Minuta minuta = pegaUltimaMinutaByProcesso(idProcesso);
        setMovimentosEhComplementosFromTpu(minuta);
        try {
            minuta.renderizarHtml(modeloDocumento, usuario, minuta.getProcesso(), false);
        } catch (Exception e) {
            throw new Exception(String.format("Não foi possível renderizar a minuta: %d", minuta.getId()), e);
        }
        MinutaMovimento.ordenaPorOrdem(minuta.getMinutaMovimentos());
        MinutaAnexo.ordenaPorPosicao(minuta.getAnexos());
        return minuta;
    }

    public void setCabecalho(Minuta minuta) {
        if (minuta.getMinutaHtml() == null) {
            ModeloMinutaCabecalhoRodape cabecalhoERodape = this.modeloMinutaService
                .getCabecalhoERodape();
            minuta.setMinutaHtml(cabecalhoERodape.getCabecalho() + cabecalhoERodape.getRodape());
        }
    }


    public void setMovimentosEhComplementosFromTpu(Minuta minuta) throws Exception {
        List<MinutaMovimento> minutaMovimentos = minuta.getMinutaMovimentos();
        if(minutaMovimentos != null) {
            List<Long> codigos = minutaMovimentos.stream().map(dm -> dm.getMovimentoId()).collect(Collectors.toList());
            List<TpuMovimento> movimentos = tpuMovimentoService.getMovimentosComComplemento(codigos);

            minutaMovimentos.forEach(mm -> movimentos.stream()
                .filter(mov -> mov.getCodigo().equals(mm.getMovimentoId()))
                .findFirst()
                .ifPresentOrElse(tm -> {
                    mm.setDescricao(tm.getDescricao());
                    tm.getComplementos().forEach(tpuComplemento -> {
                        if (mm.getMinutaMovimentoComplementos() != null)
                            mm.getMinutaMovimentoComplementos().stream()
                                .filter(mmc -> mmc.getComplementoId().equals(tpuComplemento.getCodigo())).findAny().ifPresentOrElse(
                                minutaMovimentoComplemento -> minutaMovimentoComplemento.setDadosTpu(tpuComplemento),
                                () -> mm.getMinutaMovimentoComplementos().add(new MinutaMovimentoComplemento(tpuComplemento))
                            );
                    });
                }, () -> mm.setDescricao("")));
        }
    }

    /**
     * Esse metodo Não deveria existir, o objeto minuta deveria deixar de ser anemico
     * E o estado do banco não deve ser testado,
     * contudo os objetos não poderiam ser construidos com estado invalido (unitTest).
     *
     * @param minuta
     * @throws Exception
     */
    public void verificaIntegridadeDoBancoAoSalvar(Minuta minuta) throws Exception {
//        verificaSeMinutaUnicoAtivoNoProcesso(minuta);
        verificaSeMinutaTemAnexoUploadOuEhHtml(minuta);
        verificaSeMinutaTemIdProcesso(minuta);
        verificaSeMinutaTemMovimento(minuta);
        verificaSeTemTipoVoto(minuta);
        verificaSePodeMudarTipoDocumento(minuta);
    }

    private void verificaSePodeMudarTipoDocumento(Minuta minuta) throws Exception {
        if (minuta.getProcesso().temTag("EM_SESSAO")) {
            minutasRepository.findById(minuta.getId()).ifPresent(minutaSalva -> {
                if (minutaSalva.getTipoDocumento().equals(minuta.getTipoDocumento()))
                    throw new RuntimeException("Não é possível alterar o tipo de documento de uma minuta em sessão");
            });
        }
    }

    private void verificaSeTemTipoVoto(Minuta minuta) throws Exception {
        final Optional<String> tipoVoto = minuta.getQuestions().stream()
            .filter(q -> q.getKey().equals("tipoVoto"))
            .findAny()
            .flatMap(q -> ofNullable(q.getValue()));

        if (minuta.getTipoDocumento().isMinutaColegiado())
            if (tipoVoto.isEmpty()) throw new Exception("Tipo Voto está em branco!");
    }

    private void verificaSeMinutaTemAnexoUploadOuEhHtml(Minuta minuta) throws Exception {
        boolean algumInvalido = minutaAnexoService.getAnexosByminutaPai(minuta).stream().anyMatch(anexo -> !anexo.isValid());
        if (algumInvalido) {
            throw new Exception("Há anexos inválidos na minuta");
        }
    }

    private void verificaSeMinutaTemMovimento(Minuta minuta) throws Exception {
        List<MinutaMovimento> movimento = minutaMovimentoRepository.findByMinuta(minuta);
        if (movimento == null || movimento.size() <= 0)
            throw new Exception("A minuta deve ter ao menos um movimento");
    }

    private void verificaSeMinutaTemIdProcesso(Minuta minuta) {
        String msg = "Operação abortada, não foi encontrado referencia para o ID do Processo";
        if (minuta.getProcesso() == null || minuta.getProcesso().getId() == null)
            throw new RuntimeException(msg);
    }

    private void verificaSeMinutaUnicoAtivoNoProcesso(Minuta minuta) throws Exception {
        Minuta minAux = null;
        Usuario usuario = auth.getUsuario(SecurityContextHolder.getContext().getAuthentication());
        try {
            minAux = recuperaMinutaNaoAssinadoDoProcesso(usuario, minuta.getProcesso().getId());
        } catch (Exception e) {
        }
        if (minAux != null && !minuta.equals(minAux)) {
            String msg = "O Processo de id: " + minuta.getProcesso().getId();
            msg += " tem mais de uma minuta ativa";
            throw new RuntimeException(msg);
        }
    }

    private Minuta removeAtributosQueNaoDevemSerSalvos(Minuta minuta) {
        minuta.setAnexos(null);
        return minuta;
    }

    public void removeMovimento(Long idMinuta, Long idMovimento) throws Exception {
        List<MinutaMovimento> minMov = minutaMovimentoRepository.findByMinutaIdAndMovimentoId(idMinuta,
            idMovimento);
        minutaMovimentoRepository.deleteAll(minMov);
    }

    public MinutaMovimento adicionaMovimento(Long idMinuta, Long codigoMovimento) throws Exception {
        TpuMovimento tpuMovimento = tpuMovimentoService.getMovimentoComComplementos(codigoMovimento);
        MinutaMovimento minutaMovimento;
        if (idMinuta == 0) {
            minutaMovimento = new MinutaMovimento(idMinuta, codigoMovimento);
        } else {
            minutaMovimento = minutaMovimentoRepository.save(new MinutaMovimento(idMinuta, codigoMovimento));
        }
        if (minutaMovimento.getMinutaMovimentoComplementos() == null)
            minutaMovimento.setMinutaMovimentoComplementos(new ArrayList<>());
        if(tpuMovimento != null) {
            minutaMovimento.setDescricao(tpuMovimento.getDescricao());
            if (tpuMovimento.getComplementos() != null)
                tpuMovimento.getComplementos().forEach(tpuComplemento -> minutaMovimento.getMinutaMovimentoComplementos().stream()
                    .filter(mmc -> mmc.getComplementoId().equals(tpuComplemento.getCodigo())).findAny().ifPresentOrElse(
                        minutaMovimentoComplemento -> minutaMovimentoComplemento.setDadosTpu(tpuComplemento),
                        () -> minutaMovimento.getMinutaMovimentoComplementos()
                            .add(new MinutaMovimentoComplemento(tpuComplemento))
                    ));
        }

        return minutaMovimento;
    }

    @Deprecated
    public List<Long> minutarEmLote(Usuario usuario, MinutaEmLote minutaEmLote) throws Exception {
        Minuta minutaFake = minutaEmLote.getMinuta();
        Map<Long, List<MinutaMovimento>> movimentos = minutaEmLote.getMovimentos();
        Set<Long> idsProcessos = movimentos.keySet();
        minutaFake.getMinutaMovimentos().forEach(mm -> mm.setMinuta(null));
        List<Long> resp = new ArrayList<>();
        for (Long idProcesso : idsProcessos) {
            try {
                Minuta minuta = pegaUltimaMinutaByProcesso(idProcesso);
                minuta.defineAutoria(usuario);
                minuta.copiaPropriedades(minutaFake);
                this.renderizaHtml(minuta, usuario);
                this.salvaDJE(minuta, minutaFake);
                this.salvarMovimentos(minuta, movimentos);
                minuta = salvarComVersao(minuta, usuario);
                this.verificaIntegridadeDoBancoAoSalvar(minuta);
                this.processoService.enviaProcessoTarefaAssinar(minuta.getProcesso(),usuario);
                resp.add(idProcesso);
            } catch (Exception e) {
                this.looger.error(String.format("Erro ao minutar em lote o processo %d", idProcesso), e);
            }
        }
        return resp;
    }

    private void salvarMovimentos(Minuta minuta, Map<Long, List<MinutaMovimento>> movimentos) {
        this.minutaMovimentoRepository.deleteAllByMinuta(minuta);
        minuta.setMinutaMovimentos(new ArrayList<>());
        Minuta finalMinuta = minuta;
        this.minutaMovimentoRepository.saveAll(movimentos.get(minuta.getIdProcesso()).stream().map(m -> {
            m.setMinuta(finalMinuta);
            return m;
        }).collect(Collectors.toList()));
    }

    private void salvaDJE(Minuta minuta, Minuta minutaFake) {
        PublicarProcessoDJE publicarProcessoDJE = minuta.getPublicacaoDje();
        if (publicarProcessoDJE == null) {
            publicarProcessoDJE = new PublicarProcessoDJE();
        }
        publicarProcessoDJE.copiaPropriedades(minutaFake.getPublicacaoDje());
        publicarProcessoDJE.setMinuta(minuta);
        this.publicarProcessoDjeService.save(publicarProcessoDJE);
    }

    private void renderizaHtml(Minuta minuta, Usuario usuario) throws Exception {
        try {
            minuta.renderizarHtml(modeloDocumento, usuario, minuta.getProcesso(), false);
        } catch (Exception e) {
            throw new Exception(String.format("Não foi possível renderizar a minuta: %d", minuta.getId()), e);
        }
    }

    @Deprecated
    public Minuta salvaHtmlRenderizado(Minuta minuta, String modeloProcessado) {
        minuta.setMinutaHtmlRenderizado(modeloProcessado);
        Minuta saved = save(minuta);
        return saved;
    }

    public Minuta salvaHtmlRenderizado(Minuta minuta,Usuario usuario, Processo processo, Boolean assinando) throws ServicoRemotoException {
        minuta.renderizarHtml(modeloDocumento, usuario, processo, assinando);
        Minuta saved = save(minuta);
        return saved;
    }

    public Minuta criaMinutaVoid(Processo processo,Usuario usuarioLogado, ProcessoConcluso processoConcluso) throws Exception {
        Minuta minuta;
        if(processoConcluso != null && (processoConcluso.getModeloDocumentoHtmlPje()!=null || processoConcluso.getTiposDocumentos()!=null)){
            if (!processo.minutaEmElaboracao().isPresent()) {
                minuta = new Minuta(new Processo(processo.getId()));
            } else {
                minuta = processo.minutaEmElaboracao().get();
            }
            if(processoConcluso.getModeloDocumentoHtmlPje() != null) {
                minuta.setMinutaHtml(processoConcluso.getModeloDocumentoHtmlPje());
            }

            if(processoConcluso.getTiposDocumentos() != null){
                minuta.setTiposDocumentos(processoConcluso.getTiposDocumentos());
            }

            save(minuta);
            minutaTarefaLogService.save(new MinutaTarefaLog(minuta, usuarioLogado, processo.getTarefa()));
        }else{
            if (!processo.minutaEmElaboracao().isPresent()) {
                minuta = new Minuta(new Processo(processo.getId()));
                save(minuta);
                minutaTarefaLogService.save(new MinutaTarefaLog(minuta, usuarioLogado, processo.getTarefa()));
            } else{
                minuta = processo.minutaEmElaboracao().get();
            }
        }
        return  minuta;
    }
}
