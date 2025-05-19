package br.jus.tjro.gabinete.config.security.manager;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class DevAuthenticateManager implements AuthenticationManager {

    private final List<String> allOrgaoJulgador;

    public DevAuthenticateManager(ProcessosRepository processosRepository){
        allOrgaoJulgador = processosRepository.allOrgaoJulgador();
    }
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        authentication.setAuthenticated(true);
        Usuario usuario = AuthenticationUsuarioService.getUsuarioMock();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new DevAuthentication(usuario);
    }
}
