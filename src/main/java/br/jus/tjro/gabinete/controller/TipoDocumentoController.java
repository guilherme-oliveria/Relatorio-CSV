package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;

import br.jus.tjro.gabinete.service.local.TipoDocumentoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("tipo-documento")
public class TipoDocumentoController {

    @Autowired
    private TipoDocumentoService tipoDocumentoService;

    @GetMapping("/")
    public ResponseEntity<List<TipoDocumento>> tipoDocumento() {
        List<TipoDocumento> tipoDocumentos = tipoDocumentoService.pegaTodos();
        return ResponseEntity.ok().body(tipoDocumentos);
    }

    @GetMapping("/inclusive-minuta")
    public ResponseEntity<List<TipoDocumento>> tipoDocumentoInclusiveMinuta() {
        List<TipoDocumento> tipoDocumentos = tipoDocumentoService.pegaTodosInclusiveMinuta();
        return ResponseEntity.ok().body(tipoDocumentos);
    }

    @GetMapping("minuta")
    public ResponseEntity<List<TipoDocumento>> tipoDocumentoMinuta() {
        List<TipoDocumento> tipoDocumentos = tipoDocumentoService.pegaTodosMinuta();
        return ResponseEntity.ok().body(tipoDocumentos);
    }

    @GetMapping("minutaCaixa/{idCaixa}")
    @Cacheable(value = "tipoDocumentoCache", key = "#idCaixa", unless = "#result == null or #result.body.isEmpty()")
    public ResponseEntity<List<TipoDocumento>> tipoDocumentoIdCaixa(@PathVariable("idCaixa") String idCaixa) throws ServicoRemotoException {
        List<TipoDocumento> tipoDocumentos = tipoDocumentoService.buscaTipoDocumentoCaixa(Integer.parseInt(idCaixa));
        return ResponseEntity.ok().body(tipoDocumentos);
    }

    @GetMapping("minuta/{idMinuta}")
    public ResponseEntity<List<TipoDocumento>> tipoDocumentoIdMinuta(@PathVariable("idMinuta") String idMinuta) throws ServicoRemotoException {
        List<TipoDocumento> tipoDocumentos = tipoDocumentoService.buscaTipoDocumentoMinuta(Long.parseLong(idMinuta));
        return ResponseEntity.ok().body(tipoDocumentos);
    }


}
