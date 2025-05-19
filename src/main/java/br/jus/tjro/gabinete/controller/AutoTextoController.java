package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.AutoTexto;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.local.AutoTextoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("autotexto")
public class AutoTextoController {

    private final AutoTextoService autoTextoService;

    private final AuthenticationUsuarioService auth;

    @Autowired
    public AutoTextoController(AutoTextoService autoTextoService,AuthenticationUsuarioService auth){
        this.autoTextoService = autoTextoService;
        this.auth = auth;
    }

    @GetMapping("/meus")
    public ResponseEntity<List<AutoTexto>> listaMeuAutoTexto(Authentication authentication) throws Exception {
        Usuario usuarioLogado = auth.getUsuario(authentication);
        List<AutoTexto> listaTodosAutotextosDoUsuario = autoTextoService.buscaTodosDoUsuario(usuarioLogado);
        return listaTodosAutotextosDoUsuario.size() > 0 ? ResponseEntity.ok(listaTodosAutotextosDoUsuario) : ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("/orgao_julgador/{oj}")
    public ResponseEntity<List<AutoTexto>> listaAutoTextoOj(@PathVariable("oj") String oj, Authentication authentication) throws Exception {
        Usuario usuarioLogado = auth.getUsuario(authentication);
        List<AutoTexto> listaDosAutotextosDoOj = autoTextoService.buscaTodosDoOJExcetoDoUsuario(oj, usuarioLogado);
        return listaDosAutotextosDoOj.size() > 0 ? ResponseEntity.ok(listaDosAutotextosDoOj) : ResponseEntity.ok(new ArrayList<>());
    }

    @PostMapping
    public ResponseEntity<AutoTexto> salvaAutoTexto(@RequestBody AutoTexto autoTexto, Authentication authentication) throws Exception {
        Usuario usuario = auth.getUsuario(authentication);
        if(autoTexto.getId() == null && autoTexto.getIdUsuario() == null){
            autoTexto.setIdUsuario(usuario.getId());
        }
        AutoTexto autoTextoSalvo = autoTextoService.salvaAutoTexto(autoTexto);
        return autoTextoSalvo != null ? ResponseEntity.ok(autoTextoSalvo) : ResponseEntity.noContent().build();
    }

    @PostMapping("/excluir/{id}")
    public ResponseEntity<Boolean> excluir(@PathVariable("id") Long id, @RequestBody AutoTexto autoTexto, Authentication authentication){
        Usuario usuario = auth.getUsuario(authentication);
        this.autoTextoService.exlcuir(autoTexto, usuario);
        return ResponseEntity.ok(true);
    }

}
