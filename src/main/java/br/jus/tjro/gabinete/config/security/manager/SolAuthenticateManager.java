package br.jus.tjro.gabinete.config.security.manager;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SolAuthenticateManager implements AuthenticationManager {

    private final JwtDecoder decode;

    public SolAuthenticateManager(JwtDecoder decoder){
        this.decode = decoder;
    }

    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(authentication instanceof BearerTokenAuthenticationToken){
            BearerTokenAuthenticationToken bearerAuthentication = ((BearerTokenAuthenticationToken) authentication);
            Usuario user = decode.converteTokenEhValida(bearerAuthentication.getToken());
            if(user != null)
                return new UsernamePasswordAuthenticationToken(user.getId(), user, user.authority());
            else
                return null;
        }
        throw new AuthenticationServiceException("Não foi possivel realizar o login, o objeto Authentication não é do tipo esperado");
    }
}
