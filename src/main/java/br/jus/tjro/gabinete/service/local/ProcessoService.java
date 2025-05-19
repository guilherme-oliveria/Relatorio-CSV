package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.dto.TagDTO;
import br.jus.tjro.gabinete.dto.TagProcessoDTO;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.localizador.Caixa;
import br.jus.tjro.gabinete.model.gab.processo.PrioridadeProcessual;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoTag;
import br.jus.tjro.gabinete.model.gab.tpu.TpuAssunto;
import br.jus.tjro.gabinete.model.gab.tpu.TpuClasse;
import br.jus.tjro.gabinete.model.gab.transiente.ProcessoConcluso;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.repository.gab.filter.ProcessoFilter;
import br.jus.tjro.gabinete.repository.gab.processo.PrioridadesProcessuaisRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoTagRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.scheduled.TodosScheduled;
import br.jus.tjro.gabinete.scheduled.devolve.origem.DevolveOrigem;
import br.jus.tjro.gabinete.service.JobsService;
import br.jus.tjro.gabinete.service.local.processo.documento.ProcessoDocumentoService;
import br.jus.tjro.gabinete.service.local.tpu.TpuAssuntoService;
import br.jus.tjro.gabinete.service.local.tpu.TpuClasseService;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import br.jus.tjro.gabinete.service.remoto.processo.ProcessoRemotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProcessoService {
    private final KafkaProducerService kafkaProducerService;
    private final ApplicationEventPublisher eventPublisher;
    private final MinutaTarefaLogService minutaTarefaLogService;
    private final CaixaService caixaService;
    private String tribunal;

    private final ProcessoDocumentoService processoDocumentoService;

    @Autowired
    public ProcessoService(KafkaProducerService kafkaProducerService,
                           ApplicationEventPublisher eventPublisher,
                           MinutaTarefaLogService minutaTarefaLogService,
                           CaixaService caixaService, @Value("${tribunal:''}") String tribunal,
                           ProcessoDocumentoService processoDocumentoService){
        this.kafkaProducerService = kafkaProducerService;
        this.eventPublisher = eventPublisher;
        this.minutaTarefaLogService = minutaTarefaLogService;
        this.caixaService = caixaService;
        this.tribunal = tribunal;
        this.processoDocumentoService = processoDocumentoService;
    }

    @Autowired
    private ProcessosRepository processosRepository;

    @Autowired
    private PrioridadesProcessuaisRepository prioridadesProcessuaisRepository;

    @Autowired
    private TpuClasseService TpuService;

    @Autowired
    private TpuAssuntoService tpuAssuntoService;

    @Autowired
    private JobsService jobsService;

    @Autowired
    private TarefaService tarefaService;

    @Autowired
    private TodosScheduled todosScheduled;

    @Autowired
    private ProcessoTagRepository processoTagRepository;

    @Autowired
    private ProcessoRemotoService processoRemotoService;

    private final Logger looger = LoggerFactory.getLogger(ProcessoService.class);

    public Processo save(Processo entity) {
        return processosRepository.save(entity);
    }

    @PostAuthorize("validaProcessoAcessado(returnObject)")
    public Processo findOne(Long id) throws Exception {
        Processo processo = processosRepository.findById(id).orElseThrow(NullPointerException::new); // ToDo tratar a falta desse recurso
        return processo;
    }


    @PostAuthorize("validaProcessoAcessado(returnObject)")
    public Optional<Processo> findById(Long id) {
        return processosRepository.findById(id);
    }

    public void setDescricaoAssuntosEmProcesso(Processo processo, List<TpuAssunto> tpuAssuntos) throws Exception {
        Map<Long, String> tpuDict = new HashMap<>();
        tpuAssuntos.stream().filter(it -> it != null && it.getCodigo() != null && it.getDescricao() != null)
            .forEach(tpuAssunto -> tpuDict.put(tpuAssunto.getCodigo(), tpuAssunto.getDescricao()));
        processo.getProcessoAssuntos().stream().forEach(processoAssunto -> {
            String descricao = tpuDict.getOrDefault(processoAssunto.getIdAssunto(), "");
            processoAssunto.setDescricao(descricao);
        });
        processo.setTpuClasse(TpuService.getClasse(processo.getTpuClasse().getCodigo()));
    }

    public List<Processo> findAll(List<Long> ids) {
        return processosRepository.findByIds(ids);
    }

    @Deprecated
    public Page<Processo> filtrarProcessosComPaginacaoEOrgaoJulgador(ProcessoFilter processoFilter, Pageable pageable, int idCaixa, List<TarefaEnum> tarefas, List<String> idOJ) throws Exception {
        Page<Processo> page = processosRepository.filtrarProcessosComPaginacaoEOrgaoJulgador(processoFilter, pageable, idCaixa, tarefas, idOJ);
        return page;
    }

    public Long pegaIdProcessoLegado(long idProcesso) {
        return processosRepository
            .findById(idProcesso)
            .map(Processo::getIdProcessoSistemaLegado)
            .orElseThrow(NullPointerException::new); // ToDo tratar a falta desse recurso
    }

    public List<Processo> getProcessosPorTarefa(TarefaEnum tarefa) {
        return processosRepository.findByTarefaEnum(tarefa);
    }

    public List<Processo> getProcessosPorTarefas(List<TarefaEnum> tarefas) {
        return processosRepository.findByTarefaEnumIn(tarefas);
    }

    @Transactional
    public Processo atualizaOuCriaProcesso(ProcessoConcluso processoConcluso, FonteDadosEnum fonteDadosEnum,
                                           TarefaEnum tarefa, Usuario usuario) throws Exception {
        Processo processo = processosRepository.findByIdProcessoSistemaLegadoAndSistema(processoConcluso.getIdProcessoLegado(),fonteDadosEnum).orElse(new Processo());
        processo.setIdProcessoSistemaLegado(processoConcluso.getIdProcessoLegado());
        processo = montaProcesso(processoConcluso, fonteDadosEnum, processo,tarefa,usuario);
        processo = processosRepository.save(processo);
        return processo;
    }

    public Processo getProcessosPorTarefaLimitOne(TarefaEnum tarefa, String idOJ) {
        return processosRepository.findTop1ByTarefaEnumAndOrgaoJulgadorObj(tarefa,new OrgaoJulgador(idOJ));
    }

    @Deprecated
    public Processo montaProcesso(ProcessoConcluso processoConcluso,
                                  FonteDadosEnum fonteDadosEnum, Processo processo, TarefaEnum tarefaEnum, Usuario usuario) throws Exception {
        processo.setAssuntoPrincipal(processoConcluso.getAssuntoPrincipal());
        processo.setTpuClasse(new TpuClasse(Long.parseLong(processoConcluso.getClasseJudicial())));
        processo.setOrgaoJulgadorObj(new OrgaoJulgador(fonteDadosEnum+"-"+processoConcluso.getOrgaoJulgador()));
        processo.setNumeroProcesso(processoConcluso.getNumeroProcesso());
        processo.setJusticaGratuita(processoConcluso.isJusticaGratuita());
        processo.setPossuiLiminar(processoConcluso.isPossuiLiminar());
        processo.setSegredoJustica(processoConcluso.isSegredoJustica());
        processo.setCompetencia(processoConcluso.getCompetencia());
        processo.setSistema(fonteDadosEnum);
        processo.setDigital(processoConcluso.isDigital());
        processo.setNivelSigilo(processoConcluso.getNivelAcesso());
        processo.setIdColegiado(fonteDadosEnum+"-"+processoConcluso.getIdColegiado());
        processo.setMotivoSegredoJustica(processoConcluso.getMotivoSegredoJustica());

        // TODO correção provisoria para impedir que o processo mude de caixa e data de
        // conclusão
        if (processo.getTarefa() == TarefaEnum.NaoConcluso || processo.getTarefa() == null) {
            processo.setTarefa(tarefaEnum);
            minutaTarefaLogService.registrarLog(processo,usuario);
            processo.setDataEntrada(LocalDateTime.now());
        } else if (processo.getDataEntrada() == null) {
            processo.setDataEntrada(LocalDateTime.now());
        }
        if(tribunal.equals("tjmg")){
            processo.setCaixa(caixaService.selecionaCaixaPje(processo));
        }else {
            processo.setCaixa(caixaService.selecionaCaixa(processo));
        }
        processo.setDataUltimaDistribuicao(processoConcluso.getDataUltimaDistribuicao());
        processo.setValorCausa(processoConcluso.getValorCausa());
        processo.removePrioridade();
        if (processoConcluso.getListaIdsPrioridades() != null) {
            Set<PrioridadeProcessual> prioridadesInserir = new HashSet<PrioridadeProcessual>();
            for (Integer prioridade : processoConcluso.getListaIdsPrioridades()) {
                Optional<PrioridadeProcessual> prioridadeBanco = prioridadesProcessuaisRepository.findById(prioridade);
                if (!prioridadeBanco.isPresent()) {
                    jobsService.buscaPrioridadesProcessuais();
                }
                prioridadesProcessuaisRepository
                    .findById(prioridade)
                    .ifPresent(prioridadesInserir::add);
            }
            processo.setPrioridades(prioridadesInserir);
            processo.setPrioridade(true);
        }
        return processo;
    }

    public void enviaProcessoTarefaAssinar(Processo processo, Usuario usuario) throws Exception {
        processo.setTarefa(TarefaEnum.Assinar);
        minutaTarefaLogService.registrarLog(processo,usuario);
        processosRepository.save(processo);
    }

    public Processo devolveOrigemSemManifestacao(Processo processo, Usuario usuario) throws Exception {
        processo.devolveOrigemSemManifestacao();
        minutaTarefaLogService.registrarLog(processo,usuario);
        Processo processoUpdated = processosRepository.save(processo);
        kafkaProducerService.send(DevolveOrigem.TOPIC_DEVOLVE_ORIGEM,processoUpdated);
        return processoUpdated;
    }

    @Deprecated
    public int atualizaSegredoJustica(Processo processo) {
        return this.processosRepository.atualizaSegredoJustica(processo.getMotivoSegredoJustica(), processo.getId());
    }

    @Deprecated
    public int removeSegredoJustica(Processo processo) {
        return this.processosRepository.removeSegredoJustica(processo.getId());
    }

    @Deprecated
    public Page<Processo> filtrarProcessosComPaginacaoEOrgaoJulgadorDaManifestacao(ProcessoFilter processoFilter, Pageable pageable, Long idCaixa, List<String> idOJ, List<TarefaEnum> tarefas) {
        Page<Processo> page = processosRepository.filtrarProcessosComPaginacaoEOrgaoJulgador(processoFilter, pageable,
            idCaixa.intValue(), tarefas, idOJ);
        return page;
    }

    public List<Processo> findByTarefaParaOrgaoJulgador(TarefaEnum tarefaEnum, String idOrgaoJulgador) {
        return processosRepository.findByOrgaoJulgadorObjAndTarefaEnum(new OrgaoJulgador(idOrgaoJulgador), tarefaEnum);
    }

    public Processo mudaCaixaDoProcesso(Processo processo, Caixa caixa) throws Exception {
        if(processo.getTarefa() == TarefaEnum.Assinando) {
            throw new Exception("Não é possível mudar a caixa de processo que está para assinatura.");
        }
        processo.setCaixa(caixa);
        return processosRepository.save(processo);
    }

    public Processo atualizaProcessoManualmente(Processo processo, Usuario usuario) {
        try {
            (new Thread() {
                public void run() {
                    todosScheduled.executaPorProcesso(processo);
                }
            }).start();
            ProcessoConcluso processoConcluso = processoRemotoService.buscaProcessoConcluso(processo.getFonteDados(), processo.getIdProcessoSistemaLegado());
            montaProcesso(processoConcluso,processo.getFonteDados(),processo,processo.getTarefa(),usuario);
        } catch (Exception e) {
            looger.error(e.getMessage()+" Numero processo: "+processo.getNumeroProcesso(), e);
        }
        return processosRepository.save(processo);
    }


    public Optional<Processo> findByIdProcessoSistemaLegadoAndSistemaOrNumeroProcessoAndSistema(long idProcessoLegado,
                                                                                         String numeroProcesso,
                                                                                         FonteDadosEnum fonteDadosEnum){
        Optional<Processo> processo = processosRepository.findByIdProcessoSistemaLegadoAndSistema(
            idProcessoLegado, fonteDadosEnum);
        if(processo.isEmpty())
            processo = processosRepository.findByNumeroProcessoAndSistema(numeroProcesso, fonteDadosEnum);
        return processo;
    }

    public TagProcessoDTO concluirImportacaoTag(TagProcessoDTO tagProcessoDTO, Processo processo) throws Exception{
        List<ProcessoTag> processoTags = processoTagRepository.findByProcessoTagAll(processo.getId(), convertLocalDateTimeToDate(processo.getDataEntrada()));

        List<TagDTO> tagDTOList = retornaListTag(processoTags);
        if (tagDTOList.isEmpty()){
            return null;
        }else{
            tagProcessoDTO.setTagsList(tagDTOList);
            return tagProcessoDTO;
        }
    }

    public List<TagDTO> retornaListTag(List<ProcessoTag> processoTags){
        return  processoTags.stream()
            .filter(processoTag -> !processoTag.getTag().isDeSistema())
            .filter(processoTag -> !processoTag.getTag().validaApenasMeuGabinete())
            .map(processoTag -> new TagDTO(processoTag.getTag().getTag(),processoTag.getDataExclusao(), processoTag.getTag().getIdTagPje()))
            .collect(Collectors.toList());
    }

    public static Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
