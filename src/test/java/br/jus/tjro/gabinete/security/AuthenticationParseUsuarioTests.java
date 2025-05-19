package br.jus.tjro.gabinete.security;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.util.AuthenticationUtil;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthenticationParseUsuarioTests {

    @Test
    public void fazParseDeUsuarioSistema(){
        Authentication usuarioSistema = AuthenticationUtil.getAuthenticationAdmin();
        Usuario usuario = AuthenticationParseUsuario.parse(usuarioSistema);
        assertEquals("Sistema",usuario.getNome());
    }
}
