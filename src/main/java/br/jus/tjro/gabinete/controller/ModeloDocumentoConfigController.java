package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.model.docs.ModeloDocumentoEnv;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("modelodocumento")
public class ModeloDocumentoConfigController {

    private final ModeloDocumentoEnv docsEnv;

    public ModeloDocumentoConfigController(ModeloDocumentoEnv docsEnv) {
        this.docsEnv = docsEnv;
    }

    @GetMapping("/")
    public ResponseEntity pegarVariaveis() {
        return ResponseEntity.ok(new HashMap(){{
            put("modelodocumento", docsEnv);
        }});
    }

}
