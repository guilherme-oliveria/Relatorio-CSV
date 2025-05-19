package br.jus.tjro.gabinete.security;

import br.jus.tjro.gabinete.model.gab.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;


public class AuthenticationParseUsuario {


    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationParseUsuario.class);

    public static Usuario parse(Authentication auth){
        Usuario usuario = null;
        if(auth == null)
            throw new NullPointerException("Authentication is null");
        else if(auth.getCredentials() instanceof Usuario)
            usuario = (Usuario) auth.getCredentials();
        return usuario;
    }
}
