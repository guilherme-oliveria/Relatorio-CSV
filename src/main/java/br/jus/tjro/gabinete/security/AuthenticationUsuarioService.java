package br.jus.tjro.gabinete.security;

import br.jus.tjro.gabinete.config.AdminsEnvironment;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.service.local.OrgaoJulgadorService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class AuthenticationUsuarioService {

    private final OrgaoJulgadorService orgaoJulgadorService;
    private final AdminsEnvironment adminsEnvironment;

    public AuthenticationUsuarioService(OrgaoJulgadorService orgaoJulgadorService, AdminsEnvironment adminsEnvironment) {
        this.orgaoJulgadorService = orgaoJulgadorService;
        this.adminsEnvironment = adminsEnvironment;
    }

    public Usuario getUsuario(Authentication auth) {
        return this.getUsuario(auth,true);
    }
    public Usuario getUsuario(Authentication auth, boolean findPapel) {
        Usuario usuario = AuthenticationParseUsuario.parse(auth);
        if (findPapel && usuario.getPermissoes().stream().filter(p -> p.equals("SISTEMA")).count() < 1)
            usuario.setPapeis(orgaoJulgadorService.findAllPapelByUsuario(usuario));
        return usuario;
    }

    public List<String> getOrgaosUlgadoresUsuarioOrNot(Usuario usuario, String idOJ) {
        if (adminsEnvironment.isAdmin(usuario.getCpf())) {
            return Collections.emptyList();
        } else if (idOJ.equals("-1")) {
            return usuario.getOrgaosJulgadores();
        } else {
            return List.of(idOJ);
        }
    }


    public static final Usuario getUsuarioMock() {
        return new Usuario("MockUser", "1", List.of(), "");
    }

}
