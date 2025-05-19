package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.dto.UsuarioMobileDTO;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.local.UsuarioService;
import br.jus.tjro.gabinete.service.remoto.UsuarioRemotoService;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    private UsuarioService usuarioService;
    private UsuarioRemotoService usuarioRemotoService;
    private AuthenticationUsuarioService auth;

    public UsuarioController(UsuarioService usuarioService,UsuarioRemotoService usuarioRemotoService,AuthenticationUsuarioService auth) {
        this.usuarioService = usuarioService;
        this.usuarioRemotoService=usuarioRemotoService;
        this.auth=auth;
    }

    @GetMapping("/login-keycloak/{code}")
    public ResponseEntity<String> login(@PathVariable String code) throws Exception {
        return ResponseEntity.ok(usuarioService.login(code));
    }

    @GetMapping("/login-keycloak/refreshtoken/{token}")
    public ResponseEntity<String> refreshToken(@PathVariable String token) throws Exception {
        return ResponseEntity.ok(usuarioService.refreshToken(token));
    }

    @PostMapping("/logout-keycloak")
    public ResponseEntity<Object> logout(@RequestBody String requestBody) throws Exception {
       usuarioService.logout(requestBody);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/dispositivos-pareados")
    public ResponseEntity<List<UsuarioMobileDTO>>  getDispositivosPareados(Authentication user) throws Exception {
        List<UsuarioMobileDTO> list = usuarioRemotoService.getDispositivosMobile(auth.getUsuario(user).getCpf());
        return list != null ? ResponseEntity.ok(list) : ResponseEntity.noContent().build();
    }


    @GetMapping("/validarToken/{token}")
    public ResponseEntity<Boolean> validarToken(@PathVariable String token,Authentication user) {
        Boolean validado = usuarioService.validarToken(token,auth.getUsuario(user).getCpf());
        return ResponseEntity.ok(validado);
    }

}
