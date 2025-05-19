package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.dto.AvisoDTO;
import br.jus.tjro.gabinete.dto.MovimentoProcessoDTO;
import br.jus.tjro.gabinete.model.gab.Aviso;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.tpu.TpuMovimento;
import br.jus.tjro.gabinete.repository.gab.AvisoRepository;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.local.AvisoService;
import br.jus.tjro.gabinete.service.local.MovimentoFavoritoService;
import br.jus.tjro.gabinete.service.local.ProcessoMovimentoService;
import br.jus.tjro.gabinete.service.local.tpu.TpuMovimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("aviso")
public class AvisoController {

    @Autowired
    private AvisoService avisoService;

    @Autowired
    private AvisoRepository avisoRepository;

    @Autowired
    private AuthenticationUsuarioService authenticationUsuarioService;

    @GetMapping("/{id}")
    public ResponseEntity<Aviso> getAviso(@PathVariable("id") Long id) {
        Aviso aviso = avisoRepository.getReferenceById(id);
        return ResponseEntity.ok().body(aviso);
    }

    @GetMapping
    public ResponseEntity<List<Aviso>> getTodosAvisos() {
        List<Aviso> avisos = avisoRepository.findAll();
        return ResponseEntity.ok().body(avisos);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<Aviso>> getTodosAvisosAtivos() {
        List<Aviso> avisos = avisoService.getTodosAvisosAtivos();
        return ResponseEntity.ok().body(avisos);
    }

    @PostMapping
    public ResponseEntity<Aviso> criarAviso(@RequestBody AvisoDTO aviso, Authentication auth) throws IOException {
        Usuario user = authenticationUsuarioService.getUsuario(auth);
        return ResponseEntity.ok(avisoService.salvar(aviso, user));
    }

    @PutMapping
    public ResponseEntity<Aviso> atualizarAviso(@RequestBody AvisoDTO aviso, Authentication auth) throws IOException {
        Usuario user = authenticationUsuarioService.getUsuario(auth);
        Aviso avisoExiste = avisoRepository.getReferenceById(aviso.getId());
        if(avisoExiste != null) {
            return ResponseEntity.ok(avisoService.salvar(aviso, user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public void deletarAviso(@PathVariable("id") Long id) {
        avisoService.excluir(id);
    }
}
