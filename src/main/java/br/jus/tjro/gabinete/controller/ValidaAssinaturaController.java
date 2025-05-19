package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.interfaces.StorageService;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.repository.gab.minuta.MinutasRepository;
import br.jus.tjro.gabinete.service.assinaturadigital.RetornoAssinaturaService;
import br.jus.tjro.gabinete.service.local.AssinaturaService;
import br.jus.tjro.gabinete.service.local.ProcessoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("valida-assinatura")
public class ValidaAssinaturaController {

    @Autowired
    private ProcessoService processoService;

    @Autowired
    private RetornoAssinaturaService assinaturaService;

    @Autowired
    private MinutasRepository minutasRepository;

    @Autowired
    private StorageService storageService;
    
    private final Logger looger = LoggerFactory.getLogger(ValidaAssinaturaController.class);

    @GetMapping("minuta/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> validaAssinaturaMinuta(@PathVariable("id") Long idMinuta) throws Exception {
        Optional<Minuta> minuta = minutasRepository.findById(idMinuta);

        if(!minuta.isPresent())
            return ResponseEntity.ok().body("Minuta não esta presente. IdMinuta= "+idMinuta);

        byte[] mensagemoriginal = minuta.get().getMinutaHtmlRenderizadoBytes();
        byte[] documentoAssinadoAttachado = new byte[0];

        try{
            byte[] detachado = storageService.loadBytes(minuta.get().getHashP7s());
            documentoAssinadoAttachado = assinaturaService.converterParaAtachado(mensagemoriginal,detachado);
        }catch (Exception e){
            return ResponseEntity.ok().body("Não foi possivel recuperar o documento do Storage. IdMinuta= "+idMinuta);
        }

        assinaturaService.validaAssinatura(documentoAssinadoAttachado);

        return ResponseEntity.ok().body("Minuta e sua assinatura esta valida. IdMinuta= "+idMinuta);
    }
}
