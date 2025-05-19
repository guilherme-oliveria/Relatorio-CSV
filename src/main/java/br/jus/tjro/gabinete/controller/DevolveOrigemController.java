package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.filter.ProcessoFilter;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.scheduled.devolve.origem.DevolveOrigem;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

import static br.jus.tjro.gabinete.Tarefas.core.TarefaEnum.*;
import static br.jus.tjro.gabinete.scheduled.devolve.origem.DevolveOrigem.TOPIC_DEVOLVE_ORIGEM;

@Controller
@RequestMapping("devolve-origem")
public class DevolveOrigemController {

    private final DevolveOrigem devolveOrigem;

    private ProcessosRepository processosRepository;

    private KafkaProducerService kafkaProducerService;

    private AuthenticationUsuarioService auth;

    @Autowired
    public DevolveOrigemController(DevolveOrigem devolveOrigem,
                                   ProcessosRepository processosRepository,
                                   KafkaProducerService kafkaProducerService,
                                   AuthenticationUsuarioService auth) {
        this.devolveOrigem = devolveOrigem;
        this.processosRepository = processosRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.auth = auth;
    }

    @PostMapping("")
    public ResponseEntity<String> devolveProcessoOrigem(@RequestBody final Processo processo) {
        try {
            Optional<Processo> processoEntity = processosRepository.findOptionalById(processo.getId());
            boolean resultadoDevolucaoOrigem = true;
            if (processoEntity.isPresent())
                resultadoDevolucaoOrigem = devolveOrigem.devolveOrigem(processoEntity.get());
            if (!resultadoDevolucaoOrigem)
                return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("error", "Erro ao devolver origem do processo " + processo.getNumeroProcesso() + ". " + getTraceId())
                    .body(null);
            return ResponseEntity.ok("true");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<String> devolveProcessoOrigem(@PathVariable Long id) throws Exception {
        Processo processoEntity = new Processo(id);
        return devolveProcessoOrigem(processoEntity);
    }

    @GetMapping("")
    public ResponseEntity<String> checkDevolveOrigem() throws Exception {
        return ResponseEntity.ok("Its working");
    }

    @GetMapping("para-integracao")
    public ResponseEntity<Page<Processo>> processoParaIntegracao(
        @RequestHeader("oj") String idOJ,
        Authentication authentication,
        Pageable pageable
    ) {
        Page<Processo> processos = processosRepository
            .filtrarProcessosComPaginacaoEOrgaoJulgador(
            new ProcessoFilter(),
            pageable,
            0,
            Arrays.asList(ParaIntegracao, ParaIntegracaoSemManifestacao, ParaIntegracaoAlvara, ParaIntegracaoPauta),
            this.auth.getOrgaosUlgadoresUsuarioOrNot(auth.getUsuario(authentication), idOJ));

        return ResponseEntity.ok(processos);
    }

    @GetMapping("para-integracao-erro")
    public ResponseEntity<Page<Processo>> processoParaIntegracaoErro(
        @RequestHeader("oj") String idOJ,
        Authentication authentication,
        Pageable pageable) {
        ProcessoFilter processoFilter = new ProcessoFilter();
        Page<Processo> processos = processosRepository
            .filtrarProcessosComPaginacaoEOrgaoJulgador(
                processoFilter,
                pageable,
                0,
                Arrays.asList(ParaIntegracaoComErro),
                this.auth.getOrgaosUlgadoresUsuarioOrNot(this.auth.getUsuario(authentication), idOJ));
        return ResponseEntity.ok(processos);
    }

    @GetMapping("reenvia")
    @Transactional(readOnly = true)
    public ResponseEntity<String> reenviaKafka() throws Exception {
        processosRepository
            .findByTarefaEnumIn(devolveOrigem.getTarefasDisponiveis())
            .forEach(p -> kafkaProducerService.send(TOPIC_DEVOLVE_ORIGEM, p));
        return ResponseEntity.ok("Processso reenviados ao kafka");
    }

    private String getTraceId(){
        return MDC.get("X-B3-TraceId");
    }
}
