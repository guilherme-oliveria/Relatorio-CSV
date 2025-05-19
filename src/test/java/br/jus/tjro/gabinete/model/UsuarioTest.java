package br.jus.tjro.gabinete.model;

import br.jus.tjro.gabinete.UnitTest;
import br.jus.tjro.gabinete.model.gab.Papel;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.segredo.recurso.AdministradorRecurso;
import br.jus.tjro.gabinete.model.gab.segredo.recurso.AssessorRecurso;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.service.local.OrgaoJulgadorService;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    @Autowired
    private OrgaoJulgadorService orgaoJulgadorService;

    public List<OrgaoJulgador> orgao() {
        OrgaoJulgador orgao = new OrgaoJulgador("PJEPG-1");
        return List.of(orgao);
    }

    @Test
    public void testaTokenSemBearer() {
        Usuario user = new Usuario("Sistema", "999.999.999-99", List.of(""), "token");
        assertEquals("Bearer token", user.getToken());
    }

    @Test
    public void testaTokenComBearer() {
        Usuario user = new Usuario("Sistema", "999.999.999-99", List.of(""), "Bearer token");
        assertEquals("Bearer token", user.getToken());
    }

    @Test
    public void testaTokenComEspaco() {
        Usuario user = new Usuario("Sistema", "999.999.999-99", List.of(""), "Bearer token ");
        assertEquals("Bearer token", user.getToken());
    }


    @Test
    public void testSetaOrgaoJulgadorPelosPapeis() {
        OrgaoJulgador oj = new OrgaoJulgador("PJEPG-1");
        Usuario usuario = new Usuario("Tanjiro", "1", List.of(), "token");

        Papel papel = new Papel("Administrador", List.of(oj));
        Papel papel2 = new Papel("Assessor", List.of(oj));

        Map<String, List<Papel>> mapPapeis = new HashMap<String, List<Papel>>();
        mapPapeis.put("PJEPG", List.of(papel, papel2));

        usuario.setPapeis(mapPapeis);

        assertEquals(2, usuario.getPapeisBySistema("PJEPG").size());
        assertEquals(oj, usuario.getPapeisBySistema("PJEPG").get(0).getOrgaoJulgador().get(0));
        assertEquals(oj, usuario.getPapeisBySistema("PJEPG").get(1).getOrgaoJulgador().get(0));
        assertEquals(1, usuario.getOrgaosJulgadoresCompleto().size());
    }

    @Test
    public void testaQuandoEhServidor(){
        OrgaoJulgador oj = new OrgaoJulgador("PJEPG-1");
        Usuario usuario = new Usuario("Tanjiro", "1", List.of(), "token");
        Papel papel = new Papel("Administrador", List.of(oj));
        Map<String, List<Papel>> mapPapeis = new HashMap<String, List<Papel>>();
        mapPapeis.put("PJEPG", List.of(papel));

        usuario.setPapeis(mapPapeis);

        assertTrue(usuario.isServidor("PJEPG"));
    }
    @Test
    public void testaQuandoNaoEhServidor(){
        OrgaoJulgador oj = new OrgaoJulgador("PJEPG-1");
        Usuario usuario = new Usuario("Tanjiro", "1", List.of(), "token");
        Papel papel = new Papel("Administrador", List.of(oj));
        Map<String, List<Papel>> mapPapeis = new HashMap<String, List<Papel>>();
        mapPapeis.put("PJEPG", List.of(papel));

        usuario.setPapeis(mapPapeis);

        assertFalse(usuario.isServidor("PJESG"));
    }

    @Test
    public void deveRetornarFalseQuandoPapeisDoUsuarioForVazio(){
        OrgaoJulgador oj = new OrgaoJulgador("PJEPG-1");
        Usuario usuario = new Usuario("Tanjiro", "1", List.of(), "token");
        Map<String, List<Papel>> mapPapeis = new HashMap<String, List<Papel>>();
        usuario.setPapeis(mapPapeis);

        assertFalse(usuario.isServidor("PJEPG"));
    }

    @Test
    public void deveRetornarFalseQuandoPapeisDoUsuarioForNull(){
        OrgaoJulgador oj = new OrgaoJulgador("PJEPG-1");
        Usuario usuario = new Usuario("Tanjiro", "1", List.of(), "token");

        assertFalse(usuario.isServidor("PJEPG"));
    }

    @Test
    public void testaSeEhMagistrado(){
        OrgaoJulgador oj = new OrgaoJulgador("PJEPG-1");
        oj.setSistema("PJEPG");
        Usuario usuarioMagistrado = new Usuario("Tanjiro", "1", List.of(), "token");
        Papel papel = new Papel("Magistrado", List.of(oj));
        Map<String, List<Papel>> mapPapeis = new HashMap<String, List<Papel>>();
        mapPapeis.put("PJEPG", List.of(papel));
        usuarioMagistrado.setPapeis(mapPapeis);

        Usuario usuarioAssessor = new Usuario("Toguro", "1", List.of(), "token");
        Papel papelAssessor = new Papel("Assessor", List.of(oj));
        Map<String, List<Papel>> mapPapeisAssessor = new HashMap<String, List<Papel>>();
        mapPapeisAssessor.put("PJEPG", List.of(papelAssessor));
        usuarioAssessor.setPapeis(mapPapeisAssessor);

        assertTrue(usuarioMagistrado.isMagistrado(oj));
        assertFalse(usuarioAssessor.isMagistrado(oj));
    }

}
