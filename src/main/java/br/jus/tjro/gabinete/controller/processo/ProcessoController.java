package br.jus.tjro.gabinete.controller.processo;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.interfaces.Tarefa;
import br.jus.tjro.gabinete.listener.kafka.consumers.AtualizaLocalizadorKafkaListener;
import br.jus.tjro.gabinete.listener.kafka.consumers.BuscaProcessosKafkaListener;
import br.jus.tjro.gabinete.listener.kafka.consumers.AtualizaProcessoKafkaListener;
import br.jus.tjro.gabinete.listener.model.processo.listener.LocalizadorListener;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.enums.TipoLocalizadorAgrupadorEnum;
import br.jus.tjro.gabinete.model.gab.localizador.Caixa;
import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorCaixa;
import br.jus.tjro.gabinete.model.gab.processo.*;
import br.jus.tjro.gabinete.model.gab.tpu.TpuAssunto;
import br.jus.tjro.gabinete.model.gab.transiente.ProcessoConcluso;
import br.jus.tjro.gabinete.repository.gab.filter.ProcessoFilter;
import br.jus.tjro.gabinete.repository.gab.localizador.caixa.LocalizadorCaixaRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.scheduled.devolve.origem.DevolveOrigem;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.local.CaixaService;
import br.jus.tjro.gabinete.service.local.MinutaTarefaLogService;
import br.jus.tjro.gabinete.service.local.ProcessoService;
import br.jus.tjro.gabinete.service.local.ProcessoTagService;
import br.jus.tjro.gabinete.service.local.processo.BuscaProcessoTodasFontes;
import br.jus.tjro.gabinete.service.local.tpu.TpuAssuntoService;
import br.jus.tjro.gabinete.service.remoto.LocalizadorRemotoService;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import br.jus.tjro.gabinete.service.remoto.processo.ProcessoRemotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("processos")
public class ProcessoController {

    private final BuscaProcessoTodasFontes buscaProcesso;
    private final KafkaProducerService kafka;
    private final LocalizadorListener localizadorListener;

    @Autowired
    public ProcessoController(BuscaProcessoTodasFontes buscaProcesso, KafkaProducerService kafka, LocalizadorListener localizadorListener){
        this.buscaProcesso = buscaProcesso;
        this.kafka = kafka;
        this.localizadorListener = localizadorListener;
    }

    @Autowired
    private LocalizadorCaixaRepository localizadorCaixaService;

    @Autowired
    private ProcessoService processoService;

    @Autowired
    private ProcessoRemotoService processoRemotoService;

    @Autowired
    private CaixaService caixaService;

    @Autowired
    private MinutaTarefaLogService minutaTarefaLogService;

    @Autowired
    private ProcessosRepository processoRepository;

    @Autowired
    private ProcessoTagService processoTagService;

    @Autowired
    private TpuAssuntoService tpuAssuntoService;

    @Autowired
    private List<Tarefa> tarefas;

    @Autowired
    private DevolveOrigem devolveOrigem;

    @Autowired
    private AuthenticationUsuarioService auth;

    @Autowired
    private LocalizadorRemotoService localizadorRemotoService;

    @GetMapping("/{id}")
    public ResponseEntity<Processo> processo(@PathVariable("id") long id) throws Exception {
        Processo processo = processoService.findOne(id);
        if (processo != null) {
            this.setDescricaoAssuntosNoProcesso(processo);
            return ResponseEntity.ok(processo);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    //TODO remover isso e fazer um proxy para o hibernate
    private void setDescricaoAssuntosNoProcesso(Processo processo) throws Exception {
        if (processo.getProcessoAssuntos() != null) {
            List<Long> codigosAssuntos = processo.getProcessoAssuntos().stream().map(ProcessoAssunto::getIdAssunto).collect(Collectors.toList());
            List<TpuAssunto> tpuAssuntos = tpuAssuntoService.getAssuntos(codigosAssuntos);
            processoService.setDescricaoAssuntosEmProcesso(processo, tpuAssuntos);
        }
    }

    @GetMapping("/lista/{ids}")
    public ResponseEntity<List<Processo>> processos(@PathVariable("ids") List<Long> ids) {
        List<Processo> processos = processoService.findAll(ids);
        return processos != null ? ResponseEntity.ok(processos) : ResponseEntity.noContent().build();
    }

    @GetMapping("/listaPaginada/{idCaixa}/{idTarefa}")
    public Page<Processo> listaProcessosConclusosPorCaixaTarefaOrgaoJulgador(
        ProcessoFilter processoFilter,
        Pageable pageable, @PathVariable("idCaixa") int idCaixa,
        @PathVariable("idTarefa") String idTarefa,
        @RequestHeader("oj") String idOJ, Authentication user) throws Exception {
        var tarefas = tarefaOuTodas(idTarefa);
        return processoService.filtrarProcessosComPaginacaoEOrgaoJulgador(processoFilter, pageable, idCaixa, tarefas,
            getOrgaosUlgadoresUsuarioOrNot(user,idOJ));
    }

    @GetMapping("/lista-paginada-erro-integracao")
    public Page<Processo> listaProcessosComProblema(ProcessoFilter processoFilter, Pageable pageable, @RequestHeader("oj") String idOJ) throws Exception {
        List<TarefaEnum> tarefas = new ArrayList<>();
        tarefas.add(TarefaEnum.ParaIntegracaoComErro);
        return processoService.filtrarProcessosComPaginacaoEOrgaoJulgador(processoFilter, pageable, 0, tarefas,
            List.of(idOJ));
    }

    @GetMapping("/pesquisa-global")
    @Transactional(readOnly = true)
    public Page<Processo> pesquisaGlobal(@RequestParam(value = "numeroProcesso", required = false) String numeroProcesso, Pageable pageable, Authentication user) throws Exception {
        Page<Processo> processos = processoRepository.findByNumeroProcesso(numeroProcesso.trim(),this.auth.getUsuario(user),pageable);
        if(processos.isEmpty()) {
            Optional<ProcessoConcluso> processoOpt = buscaProcesso.findByNumeroRemoto(numeroProcesso);
            processoOpt.ifPresent(processoConcluso -> kafka.send(BuscaProcessosKafkaListener.TOPIC_NAME, new WrapperBuscaProcesso(processoConcluso,this.auth.getUsuario(user))));
        }
        return processos;
    }


    @GetMapping("/obter-por-tag/{id-tag}")
    public List<Processo> pesquisaPorTag(@PathVariable(value = "id-tag") Long idTag) {
        return processoTagService.obterProcessosPorTagId(idTag);
    }

    @GetMapping("/devolve-sem-manifestacao/{idProcesso}")
    public ResponseEntity<Boolean> devolveOrigemSemManifestacao(@PathVariable("idProcesso") Long idProcesso,
                                                                Authentication user) throws Exception {
        Processo processo = processoService.findOne(idProcesso);
        Processo p = processoService.devolveOrigemSemManifestacao(processo, auth.getUsuario(user));
        localizadorListener.send(p);
        return ResponseEntity.ok(true);
    }

    @Transactional(readOnly = true)
    @GetMapping("/localizador-v3/listaPaginada/{id}/{tipo}/{idTarefa}")
    public Page<Processo> listaProcessosConclusosLocalizadorCaixaTarefaV3(ProcessoFilter processoFilter,
                                                        Pageable pageable,
                                                        @PathVariable("id") Long id,
                                                        @PathVariable("tipo") TipoLocalizadorAgrupadorEnum tipo,
                                                        @PathVariable("idTarefa") String idTarefa,
                                                        @RequestHeader("oj") String idOJ,
                                                        Authentication user) throws Exception {

        if(tipo == TipoLocalizadorAgrupadorEnum.Localizador) {
            LocalizadorCaixa localizador = localizadorCaixaService.findById(id)
                .orElseThrow(() -> new Exception("Erro ao selecionar localizador"));
            List<Long> processosLocalizados = localizadorRemotoService.buscaProcessoLocalizador(localizador);
            var tarefas = tarefaOuTodas(idTarefa);
            var orgaos = getOrgaosUlgadoresUsuarioOrNot(user,idOJ);
            Page<Processo> page = processoRepository.filtrarProcessosComPaginacaoEOrgaoJulgadorDoLocalizadorV3(
                processoFilter,
                pageable,
                processosLocalizados,
                tarefas,
                orgaos);
            var semFiltros = processoFilter.isEmpty() && idTarefa.equals("todos") && !idOJ.equals("-1");
            var qnteDiferentes = page.getTotalElements() != processosLocalizados.size();

            if(semFiltros && qnteDiferentes) {
               kafka.send(AtualizaLocalizadorKafkaListener.TOPIC_NAME, localizador);
            }

            return page;
        } else {
            return processoRepository.filtrarProcessosComPaginacaoEOrgaoJulgador(processoFilter, pageable,
                id.intValue(), tarefaOuTodas(idTarefa), getOrgaosUlgadoresUsuarioOrNot(user,idOJ));
        }
    }


    //TODO mudar end point para adiciona segredo
    @PatchMapping("/atualizar-sigilo")
    public int atualizarSegredoJustica(@RequestBody Processo processo, Authentication user) throws Exception {
        Processo p = this.processoRepository.findById(processo.getId())
            .orElseThrow(() -> new Exception("Processo não encontrado na base de dados para o id" + processo.getId()));
        p.adicionaSegredoJustica(processo.getMotivoSegredoJustica());
        processoRemotoService.atualizarSegredoJustica(p, p.getFonteDados(), auth.getUsuario(user));
        localizadorListener.send(p);
        return this.processoRepository.atualizaSegredoJustica(p.getMotivoSegredoJustica(), p.getId());
    }

    //TODO mudar url para remove segerdo
    @DeleteMapping("/remover-sigilo/{idProcesso}")
    public void removerSegredoJustica(@PathVariable("idProcesso") long idProcesso, Authentication user) throws Exception {
        Processo p = this.processoRepository.findById(idProcesso)
            .orElseThrow(() -> new Exception("Processo não encontrado na base de dados para o id" + idProcesso));
        p.removeSegredoJustica();
        p.setMotivoSegredoJustica("");
        processoRemotoService.atualizarSegredoJustica(p, p.getFonteDados(), auth.getUsuario(user));
        localizadorListener.send(p);
        this.processoRepository.removeSegredoJustica(idProcesso);
    }


    @GetMapping("/id-legado/{idProcessoLegado}/fonte-dados/{fonteDeDados}")
    public ResponseEntity<Processo> buscaProcessoPorIdLegadoEhFontedeDados(
        @PathVariable("idProcessoLegado") Long idProcessoLegado,
        @PathVariable("fonteDeDados") FonteDadosEnum fonteDeDados) throws Exception {
        Processo processo = this.processoRepository.findByIdProcessoSistemaLegadoAndSistema(idProcessoLegado, fonteDeDados).get();
        return ResponseEntity.ok(processo);
    }

    @PostMapping("/trocar-caixa/{idProcesso}/{idCaixa}")
    @Transactional
    public ResponseEntity<Processo> mudarCaixaDoProcesso(
        @PathVariable("idProcesso") Long idProcesso,
        @PathVariable("idCaixa") Integer idCaixa,
        Authentication authentication) throws Exception {
        Usuario usuario = auth.getUsuario(authentication);
        Caixa caixa = caixaService.findById(idCaixa).get();
        Processo processo = processoService.findOne(idProcesso);
        Processo processoUpdated = processoService.mudaCaixaDoProcesso(processo, caixa);
        minutaTarefaLogService.registrarLog(processo, usuario);
        localizadorListener.send(processoUpdated);
        return ResponseEntity.ok(processo);
    }

    private List<TarefaEnum> tarefaOuTodas(String idTarefa) {
        List<TarefaEnum> tarefas;
        if (!idTarefa.equals("todos")) {
            tarefas = new ArrayList<TarefaEnum>();
            tarefas.add(TarefaEnum.fromString(idTarefa));
        } else {
            tarefas = getTodasTarefasEnumConcluso();
        }
        return tarefas;
    }

    private List<TarefaEnum> getTodasTarefasEnumConcluso() {
        List<TarefaEnum> tarefas = new ArrayList<>();
        this.tarefas.stream().filter(p -> p.isTarefaVisivel()).forEach(m -> tarefas.add(m.getTarefa()));
        return tarefas;
    }

    @GetMapping("/atualiza-processo/{idProcesso}")
    public ResponseEntity<Boolean> atualizaProcessoManualmente(@PathVariable("idProcesso") Long idProcesso, Authentication authentication) throws Exception {
        Usuario usuario = (Usuario) authentication.getCredentials();
        WrapperAtualizaProcesso wrapperAtualizaProcesso = new WrapperAtualizaProcesso(idProcesso, usuario);
        this.kafka.send(AtualizaProcessoKafkaListener.TOPIC_NAME, wrapperAtualizaProcesso);
        return ResponseEntity.ok().body(true);
    }

    @GetMapping("/atualiza-processo-retorno/{idProcesso}")
    public ResponseEntity<Processo> atualizaProcesso(@PathVariable("idProcesso") Long idProcesso, Authentication authentication) throws Exception {
        Usuario usuario = (Usuario) authentication.getCredentials();
        Optional<Processo> processo = processoRepository.findById(idProcesso);
        if(processo.isPresent()) {
            Processo processoAtualizado = processoService.atualizaProcessoManualmente(processo.get(), usuario);
            this.setDescricaoAssuntosNoProcesso(processoAtualizado);
            localizadorListener.send(processoAtualizado);
            return ResponseEntity.ok().body(processoAtualizado);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/atualiza-processo-todos/{idProcesso}")
    public ResponseEntity<Boolean> atualizaProcessoManualmenteTodos(@PathVariable("idProcesso") Long idProcesso,Authentication authentication) throws Exception {
        return atualizaProcessoManualmente(idProcesso,authentication);
    }

    @GetMapping("/definir-nao-concluso/{idProcesso}")
    public ResponseEntity<Boolean> definirProcessoParaNaoConcluso(@PathVariable("idProcesso") Long idProcesso,
                                                                  Authentication user) {
        return processoRepository.findById(idProcesso).stream().map(processo -> {
            try {
                processo.naoConcluso();
                minutaTarefaLogService.registrarLog(processo,auth.getUsuario(user));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Processo processoUpdated = this.processoRepository.save(processo);
            localizadorListener.send(processoUpdated);
            return ResponseEntity.ok(true);
        }).findAny().orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/relacionados/{idProcesso}")
    public ResponseEntity<String[]> processosRelacionados(@PathVariable("idProcesso") Long idProcesso) {
        var processos = new String[]{""};
        var processo = processoRepository.findById(idProcesso);
        if(processo.isPresent()) {
            processos = processoRemotoService.processosRelacionados(processo.get().getIdProcessoSistemaLegado(), processo.get().getFonteDados());
        }
        return ResponseEntity.ok(processos);
    }

    private List<String> getOrgaosUlgadoresUsuarioOrNot(Authentication user, String idOJ){
        if(idOJ.equals("-1"))
            return auth.getUsuario(user).getOrgaosJulgadores();
        else
            return List.of(idOJ);
    }

}
