package br.jus.tjro.gabinete.security;

import br.jus.tjro.gabinete.config.AdminsEnvironment;
import br.jus.tjro.gabinete.model.gab.Papel;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.segredo.recurso.AssessorRecurso;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.service.local.OrgaoJulgadorService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;


public class AuthenticationUsuarioServiceTests {

    // Quando usuario tiver o perfil admin, retornar todos os OJs
    @Test
    public void ehAdmin() {
        AdminsEnvironment admins = new AdminsEnvironment(Collections.singletonList("windson"));
        OrgaoJulgadorService orgaoJulgadorService = mock(OrgaoJulgadorService.class);
        AuthenticationUsuarioService authenticationUsuarioService = new AuthenticationUsuarioService(orgaoJulgadorService, admins);
        Papel papel = new Papel("Assessor", List.of(new OrgaoJulgador("PJEPG-666")));
        Map<String, List<Papel>> mapPapeis = new HashMap<String, List<Papel>>();
        mapPapeis.put("PJEPG", List.of(papel, papel));
        Usuario windson = new Usuario("windson", "windson", Collections.emptyList(), "tchoken");
        windson.setPapeis(mapPapeis);

        assertEquals(Collections.emptyList(), authenticationUsuarioService.getOrgaosUlgadoresUsuarioOrNot(windson, "PJEPG-1"));
    }

    // Quando usuario estiver selecionado o orgao julgador "todos", e nao for admin,
    // retornar a lista de lotações do usuario, semelhante as outras listagem
    @Test
    public void naoEhAdmin() {
        AdminsEnvironment admins = new AdminsEnvironment(Collections.singletonList("gadelha"));
        OrgaoJulgadorService orgaoJulgadorService = mock(OrgaoJulgadorService.class);
        AuthenticationUsuarioService authenticationUsuarioService = new AuthenticationUsuarioService(orgaoJulgadorService, admins);
        Papel papel = new Papel("Assessor", List.of(new OrgaoJulgador("PJEPG-666"), new OrgaoJulgador("PJEPG-333")));
        Map<String, List<Papel>> mapPapeis = new HashMap<String, List<Papel>>();
        mapPapeis.put("PJEPG", List.of(papel, papel));
        Usuario windson = new Usuario("windson", "windson", Collections.emptyList(), "tchoken");
        windson.setPapeis(mapPapeis);

        assertEquals(List.of("PJEPG-666", "PJEPG-333"), authenticationUsuarioService.getOrgaosUlgadoresUsuarioOrNot(windson, "-1"));
    }

    @Test
    public void naoEhAdminSelecionouUmOj() {
        AdminsEnvironment admins = new AdminsEnvironment(Collections.singletonList("gadelha"));
        OrgaoJulgadorService orgaoJulgadorService = mock(OrgaoJulgadorService.class);
        AuthenticationUsuarioService authenticationUsuarioService = new AuthenticationUsuarioService(orgaoJulgadorService, admins);
        Papel papel = new Papel("Assessor", List.of(new OrgaoJulgador("PJEPG-666"), new OrgaoJulgador("PJEPG-333")));
        Map<String, List<Papel>> mapPapeis = new HashMap<String, List<Papel>>();
        mapPapeis.put("PJEPG", List.of(papel, papel));
        Usuario windson = new Usuario("windson", "windson", Collections.emptyList(), "tchoken");
        windson.setPapeis(mapPapeis);

        assertEquals(List.of("PJEPG-666"), authenticationUsuarioService.getOrgaosUlgadoresUsuarioOrNot(windson, "PJEPG-666"));
    }

}
