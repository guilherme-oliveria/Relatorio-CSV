package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.model.gab.tpu.TpuAssunto;
import br.jus.tjro.gabinete.service.local.ProcessoAssuntoService;
import br.jus.tjro.gabinete.service.local.tpu.TpuAssuntoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("assunto")
public class AssuntoController {

    @Autowired
    private TpuAssuntoService tpuAssuntoService;
    @Autowired
    private ProcessoAssuntoService processoAssuntoService;


    @GetMapping("/{id}")
    public ResponseEntity<TpuAssunto> assunto(@PathVariable("id") Long id) throws Exception {
        TpuAssunto assunto = this.tpuAssuntoService.getAssunto(id);
        return ResponseEntity.ok().body(assunto);
    }

    @GetMapping("/processo/{idProcesso}")
    public ResponseEntity<List<TpuAssunto>> assuntoProcesso(@PathVariable("idProcesso") Long idProcesso) throws Exception {
        List<TpuAssunto> assuntos = processoAssuntoService.buscaAssuntosProcesso(idProcesso);
        return ResponseEntity.ok().body(assuntos);
    }

    @GetMapping("/arvore")
    public ResponseEntity<List<TpuAssunto>> assuntoEmArvore() throws Exception {
        return assuntoEmArvore(null);
    }

    @GetMapping("/arvore/{id}")
    public ResponseEntity<List<TpuAssunto>> assuntoEmArvore(@PathVariable Long id) throws Exception {
        List<TpuAssunto> assuntoArvore = tpuAssuntoService.getArvoreAssuntos(id);
        return assuntoArvore != null ? ResponseEntity.ok(assuntoArvore) : ResponseEntity.noContent().build();
    }
}
