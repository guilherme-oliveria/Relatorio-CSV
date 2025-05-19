package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.service.local.verifica_integracao.VerificaIntegracaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("verifica-integracao")
public class VerificacaoIntegracaoController {

    private final ProcessosRepository processosRepository;
    private final VerificaIntegracaoService verificaIntegracaoService;

    @Autowired
    public VerificacaoIntegracaoController(ProcessosRepository processosRepository,
                                           VerificaIntegracaoService verificaIntegracaoService){
        this.processosRepository = processosRepository;
        this.verificaIntegracaoService = verificaIntegracaoService;
    }


    @GetMapping("tarefa-pje/{id}")
    public ResponseEntity<List<String>> buscaTarefaAtual(@PathVariable("id") Long idProcesso) throws ServicoRemotoException {
        Optional<Processo> processo = processosRepository.findById(idProcesso);
        if(processo.isPresent())
            return ResponseEntity.ok(verificaIntegracaoService.buscaTarefas(processo.get()));
        return ResponseEntity.notFound().build();
    }

    @GetMapping("esta-concluso-gabinete/{id}")
    public ResponseEntity<Boolean> processoTarefaPje(@PathVariable("id") Long idProcesso) throws ServicoRemotoException {
        Optional<Processo> processo = processosRepository.findById(idProcesso);
        if(processo.isPresent())
            return ResponseEntity.ok(verificaIntegracaoService.processoIsConclusoGabinete(processo.get()));
        return ResponseEntity.notFound().build();
    }
}
