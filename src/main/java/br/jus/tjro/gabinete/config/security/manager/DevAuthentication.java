package br.jus.tjro.gabinete.config.security.manager;

import br.jus.tjro.gabinete.model.gab.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DevAuthentication implements Authentication {

    private final Usuario usuario;
    private boolean logado = true;

    DevAuthentication(Usuario usuario){
        this.usuario = usuario;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<OrgaoJulgador> orgaoJulgador = usuario.getOrgaosJulgadores();
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("magistrado"));
        authorities.add(new SimpleGrantedAuthority("SISTEMA"));
        authorities.add(new SimpleGrantedAuthority("ROLE_WINDSON"));
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return usuario;
    }

    @Override
    public Object getDetails() {
        return usuario;
    }

    @Override
    public Object getPrincipal() {
        return usuario;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        logado = true;
    }

    @Override
    public String getName() {
        return usuario.getNome();
    }
}
