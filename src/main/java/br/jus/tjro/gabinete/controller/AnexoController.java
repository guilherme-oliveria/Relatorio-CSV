package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.local.MinutaAnexoService;
import br.jus.tjro.gabinete.service.local.MinutaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("anexo")
public class AnexoController {
    
    
    private final Logger looger = LoggerFactory.getLogger(AnexoController.class);

    @Autowired
    private MinutaAnexoService anexoService;

    @Autowired
    private MinutaService documentoService;

    @Autowired
    private AuthenticationUsuarioService auth;

    @GetMapping("/{idAnexo}")
    @Transactional
    public ResponseEntity<Resource> recuperaDadosAnexo(@PathVariable("idAnexo") Long id) throws Exception {
        MinutaAnexo documentoBin = anexoService.findOne(id);
        InputStreamResource resource = new InputStreamResource(anexoService.loadInputStream(documentoBin));
        String nomeArquivo = documentoBin.getDescricao() + documentoBin.getExtensao();
        return ResponseEntity.ok().header("arquivo", nomeArquivo)
            .contentType(MediaType.parseMediaType(documentoBin.getContentType())).body(resource);

    }

    @PostMapping("")
    @Transactional
    public ResponseEntity<MinutaAnexo> salvaDadosAnexo(@RequestBody MinutaAnexo documentoBin) throws Exception {
        documentoBin = anexoService.save(documentoBin);
        return ResponseEntity.ok().body(documentoBin);
    }

    @Deprecated
    @PatchMapping("/atualiza-sigilo/")
    @Transactional
    public int atualizaSigilo(@RequestBody MinutaAnexo minutaAnexo) throws Exception {
        return anexoService.atualizaSigilo(minutaAnexo.isSigilo(), minutaAnexo.getId());
    }

    @PostMapping("/reordenar/{idDocumento}")
    @Transactional
    public ResponseEntity<List<Long>> reordenarAnexos(@PathVariable("idDocumento") Long idDocumento,
                                                      @RequestBody List<Long> idsAnexos) throws Exception {
        return ResponseEntity.ok().body(anexoService.reordenar(idsAnexos));
    }

    @PostMapping(value = "/{idDocumento}/file", consumes = {"multipart/form-data;"})
    @Transactional
    public ResponseEntity<MinutaAnexo> uploadAnexo(@PathVariable("idDocumento") Long idDocumento, @RequestBody MultipartFile file) throws Exception {
        return ResponseEntity.ok().body(anexoService.save(idDocumento, file));
    }

    @PostMapping("/remove")
//    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ResponseEntity<Boolean> removerAnexo(@RequestBody Long id) {
        anexoService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @GetMapping("/lista-anexo/{idMinuta}")
    @Transactional(readOnly = true)
    public ResponseEntity<List<MinutaAnexo>> listaAnexo(@PathVariable("idMinuta") Long idMinuta, Authentication user) throws Exception {
        List<MinutaAnexo> anexos = this.anexoService.anexosFromMinuta(idMinuta);
        if(anexos.isEmpty())
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok(anexos);
    }

    @PostMapping("/remove-todos/{idMinuta}")
    public ResponseEntity<Boolean> removerTodosAnexosDaMinuta(@PathVariable("idMinuta") Long idMinuta) throws Exception {
        Minuta minuta = documentoService.findOne(idMinuta);
        anexoService.deleteAllByMinuta(minuta);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
