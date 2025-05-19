package br.jus.tjro.gabinete.util;

import br.jus.tjro.gabinete.model.gab.Usuario;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuthenticationUtil {

    public static Usuario setAuthenticationAdminInContext() {
        Usuario user = getUsuarioSistema();
        getAuthenticationAdmin(user);
        return user;
    }


    public static Authentication getAuthenticationAdmin(Usuario user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getPermissoes().forEach(i -> authorities.add(new SimpleGrantedAuthority(i.toUpperCase())));
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getId(), user, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        return auth;
    }

    public static Authentication getAuthenticationAdmin() {
       return getAuthenticationAdmin(getUsuarioSistema());
    }

    public static Usuario getUsuarioSistema() {
        List<String> permissoes = new ArrayList<String>();
        permissoes.add("SISTEMA");
        Usuario user = new Usuario("Sistema","-1",permissoes,"token");
        return user;
    }

}
