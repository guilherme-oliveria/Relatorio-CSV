package br.jus.tjro.gabinete.controller;

import br.jus.tjro.sinapses.api.modelo.ClassificacaoResultado;
import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.service.local.tpu.TpuMovimentoService;
import br.jus.tjro.gabinete.service.remoto.ia.IaClassificacaoMovimentosRemotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ia")
public class IaController {

    @Autowired
    private IaClassificacaoMovimentosRemotoService iaClassificacaoMovimentosRemotoService;

    @Autowired
    private TpuMovimentoService tpuMovimentoService;

    @PostMapping(value = "/recomendacoes")
    public ResponseEntity<ClassificacaoResultado> recomendacoes(@RequestBody String texto) throws ServicoRemotoException {
        ClassificacaoResultado iaRecomendacaoMovimento = this.iaClassificacaoMovimentosRemotoService.buscaRecomendacaoDeMovimento(texto);
        return ResponseEntity.ok().body(iaRecomendacaoMovimento);
    }

    @PostMapping(value = "/gerarTexto")
    public ResponseEntity<List<String>> gerarTexto(@RequestBody String texto) throws ServicoRemotoException {
        List<String> textos = iaClassificacaoMovimentosRemotoService.geradorTexto(texto);
        return ResponseEntity.ok().body(textos);
    }

}
