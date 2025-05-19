package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.scheduled.BuscaProcessosConclusos;
import br.jus.tjro.gabinete.scheduled.devolve.origem.DevolveOrigem;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import br.jus.tjro.gabinete.util.AuthenticationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

import static br.jus.tjro.gabinete.Tarefas.core.TarefaEnum.*;

@Controller
@RequestMapping("busca-concluso")
public class BuscaConclusoController {

    private final Logger looger = LoggerFactory.getLogger(BuscaConclusoController.class);

    @Autowired
    private BuscaProcessosConclusos buscaProcessosConclusos;


    @GetMapping("{fonteDados}/{idProcesso}")
    public ResponseEntity<Boolean> buscaConcluso(@PathVariable("fonteDados") FonteDadosEnum fonteDados, @PathVariable("idProcesso") Long id) throws Exception {
        buscaProcessosConclusos.buscaProcessosNoWebService(fonteDados,id,Minutar,AuthenticationUtil.setAuthenticationAdminInContext());
        return ResponseEntity.ok(true);
    }

    private String getTraceId(){
        return MDC.get("X-B3-TraceId");
    }
}
