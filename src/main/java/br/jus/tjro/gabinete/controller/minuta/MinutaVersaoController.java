package br.jus.tjro.gabinete.controller.minuta;

import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaVersao;
import br.jus.tjro.gabinete.service.local.MinutaService;
import br.jus.tjro.gabinete.service.local.MinutaVersaoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("minuta-versao")
public class MinutaVersaoController {

    @Autowired
    private MinutaService minutaService;

    @Autowired
    private MinutaVersaoService minutaVersaoService;
    
    private final Logger looger = LoggerFactory.getLogger(MinutaVersaoController.class);

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<MinutaVersao> minutaVersao(@PathVariable("id") Long id) {
        MinutaVersao minutaVersao;
        try {
            minutaVersao = minutaVersaoService.findOne(id);
            return ResponseEntity.ok(minutaVersao);
        } catch (Exception e) {
            looger.error(e.getMessage(),e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).body(null);
        }
    }

    @GetMapping("versao/{idMinuta}/{versao}")
    @Transactional
    public ResponseEntity<MinutaVersao> minutaVersaoCompara(@PathVariable("idMinuta") Long idMinuta,
                                                            @PathVariable("versao") Integer versao) throws Exception {
        Minuta minuta;
        minuta = minutaService.findOne(idMinuta);
        MinutaVersao minutaVersaoCompara = minutaVersaoService.findOneCompara(minuta, versao);
        return ResponseEntity.ok(minutaVersaoCompara);
    }

    @GetMapping("/lista-versoes/{idMinuta}")
    @Transactional
    public ResponseEntity<List<MinutaVersao>> listaMinutaVersoes(@PathVariable("idMinuta") Long idMinuta) {
        Minuta minuta;
        try {
            minuta = minutaService.findOne(idMinuta);
            List<MinutaVersao> versoes = minutaVersaoService.getVersaoByIdMinuta(minuta);
            return !versoes.isEmpty() ? ResponseEntity.ok(versoes) : ResponseEntity.noContent().build();
        } catch (Exception e) {
            looger.error(e.getMessage(),e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).body(null);
        }
    }

}
