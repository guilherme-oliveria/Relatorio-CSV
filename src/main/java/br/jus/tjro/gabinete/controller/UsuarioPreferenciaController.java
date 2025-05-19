package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.UsuarioPreferencia;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.local.UsuarioPreferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("preferencias")
public class UsuarioPreferenciaController {


    private final UsuarioPreferenciaService usuarioPreferenciaService;
    private final AuthenticationUsuarioService auth;

    @Autowired
    public UsuarioPreferenciaController(AuthenticationUsuarioService auth,UsuarioPreferenciaService usuarioPreferenciaService){
        this.usuarioPreferenciaService = usuarioPreferenciaService;
        this.auth = auth;
    }

    @GetMapping("/")
    public ResponseEntity<List<UsuarioPreferencia>> index(Authentication user) {
        return ResponseEntity.ok().body(this.usuarioPreferenciaService.pegaTodosDoUsuario(auth.getUsuario(user)));
    }

    @PostMapping("/")
    public ResponseEntity<UsuarioPreferencia> save(Authentication user, @RequestBody UsuarioPreferencia preferencia) {
        Usuario usuario = auth.getUsuario(user);
        return ResponseEntity.ok().body(this.usuarioPreferenciaService.salvar(usuario, preferencia));
    }

    @GetMapping("/{preferencia}")
    public ResponseEntity<UsuarioPreferencia> getByPreferencia(Authentication user, @PathVariable("preferencia") String preferencia) {
        Usuario usuario = auth.getUsuario(user);
        return ResponseEntity.ok().body(this.usuarioPreferenciaService.pegaPreferenciaDoUsuarioPorChave(usuario, preferencia));
    }
}
