package br.jus.tjro.gabinete.controller.processo;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.listener.model.processo.listener.LocalizadorListener;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosJpaRepository;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.local.MinutaService;
import br.jus.tjro.gabinete.service.local.MinutaTarefaLogService;
import br.jus.tjro.gabinete.service.local.TarefaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tarefas")
public class TarefasController {

    private final ProcessosJpaRepository processosJpaRepository;
    private final ProcessosRepository processosRepository;
    private final TarefaService tarefaService;
    private final MinutaTarefaLogService minutaTarefaLogService;
    private final MinutaService minutaService;
    private final AuthenticationUsuarioService auth;
    private final LocalizadorListener localizadorListener;

    @Autowired
    public TarefasController(ProcessosJpaRepository processosJpaRepository, ProcessosRepository processosRepository, TarefaService tarefaService,
                             MinutaTarefaLogService minutaTarefaLogService, MinutaService minutaService,
                             AuthenticationUsuarioService auth, LocalizadorListener localizadorListener) {
        this.processosJpaRepository = processosJpaRepository;
        this.processosRepository = processosRepository;
        this.tarefaService = tarefaService;
        this.minutaTarefaLogService = minutaTarefaLogService;
        this.minutaService = minutaService;
        this.auth = auth;
        this.localizadorListener = localizadorListener;
    }

    @GetMapping(headers = "Accept=application/json")
    public ResponseEntity<String> recuperaTarefasPossiveis() throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.OK).body(TarefaEnum.getValuesAsJson());
    }

    @PostMapping("/envia-assinatura")
    public ResponseEntity<Boolean> enviaAssinarDocumentos(@RequestBody Long id, Authentication usuario) throws Exception {
        Processo processo = processosJpaRepository.findById(id).orElseThrow(NullPointerException::new);
        if (processo.getTarefa() == TarefaEnum.Minutar || processo.getTarefa() == TarefaEnum.Corrigir || processo.getTarefa() == null) {
            processo.setTarefa(TarefaEnum.Assinar);
            minutaService.verificaIntegridadeDoBancoAoSalvar(processo.minutaEmElaboracao()
                .orElseThrow(() -> new Exception("Não existe minuta em elaboração para o processo "+processo.getNumeroProcesso()
                ))
            );
            minutaTarefaLogService.registrarLog(processo,auth.getUsuario(usuario));
            Processo processoUpdated = processosJpaRepository.saveAndFlush(processo);
            localizadorListener.send(processoUpdated);
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @GetMapping("/todas")
    public ResponseEntity<List<TarefaEnum>> enviaTarefas() {
        return ResponseEntity.status(HttpStatus.OK).body(tarefaService.getTarefaEnumVisiveis());

    }

    @PostMapping("/recusa-assinatura")
    public ResponseEntity<Boolean> recusaAssinaturaDocumentos(@RequestBody Long id) {
        Processo processo = processosRepository.findById(id).orElseThrow(NullPointerException::new);
        try {
            processo.recusar();
            Processo processoUpdated = processosRepository.save(processo);
            localizadorListener.send(processoUpdated);
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

}
