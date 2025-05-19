package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.model.gab.Audiencia;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.remoto.AudienciasRemotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("audiencias")
public class AudienciasController {

    private final Logger looger = LoggerFactory.getLogger(AudienciasController.class);

    private final ProcessosRepository processosRepository;
    private final AudienciasRemotoService service;
    private final AuthenticationUsuarioService auth;

    @Autowired
    public AudienciasController(ProcessosRepository processosRepository, AudienciasRemotoService service, AuthenticationUsuarioService auth) {
        this.processosRepository = processosRepository;
        this.service = service;
        this.auth = auth;
    }

    @GetMapping("/{idProcesso}")
    @Transactional
    public ResponseEntity<List<Audiencia>> getAudienciasProcesso(@PathVariable("idProcesso") Long id, Authentication user) throws Exception {
        Optional<Processo> processo = processosRepository.findById(id);
        if(processo.isPresent()){
            Usuario usuario = this.auth.getUsuario(user);
            List<Audiencia> audiencias = service.getAudiencias(processo.get(), usuario);
            return ResponseEntity.ok().body(audiencias);
        }else{
            return ResponseEntity.notFound().build();
        }

    }
}
