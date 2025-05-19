package br.jus.tjro.gabinete.util;

import br.jus.tjro.gabinete.model.gab.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthenticationUtilTests {

    @Test
    public void getUsuarioAdmin(){
        Usuario usuario = AuthenticationUtil.getUsuarioSistema();
        assertEquals("Sistema",usuario.getNome());
    }

}
