package br.jus.tjro.gabinete.model.gab.minuta.modelos;

import br.jus.tjro.gabinete.UnitTest;
import br.jus.tjro.gabinete.model.gab.Papel;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.segredo.recurso.AssessorRecurso;
import br.jus.tjro.gabinete.model.gab.segredo.recurso.MagistradoRecurso;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ModeloMinutaTests {

    @Test
    public void canEdit(){
        var resultado  = new ModeloMinuta("descricao",
            "template",
            "PJEPG-666",
            "64",
            null);
        resultado.setUsuarioSaveUpdate(getUsuario());
        OrgaoJulgador orgao = new OrgaoJulgador("PJEPG-666");
        Usuario bycpf = new Usuario("windson","cpf", List.of("juiz"),"token");

        Papel papel = new Papel("Magistrado", List.of(orgao));
        Map<String, List<Papel>> mapPapeis = new HashMap<String, List<Papel>>();
        mapPapeis.put("PJEPG", List.of(papel));
        Usuario byOrgaoJulgador = new Usuario("windson","false", List.of("juiz"),"token");
        byOrgaoJulgador.setPapeis(mapPapeis);
        Usuario falha = new Usuario("windson","cpf1", List.of("juiz"),"token");

        assertEquals(false,resultado.vara);
        assertEquals(false,resultado.meu);
        assertEquals(true,resultado.isCanEditOrDelete(bycpf));
        assertEquals(true,resultado.vara);
        assertEquals(true,resultado.meu);
        assertEquals(false,resultado.isCanEditOrDelete(falha));
        assertEquals(false,resultado.vara);
        assertEquals(false,resultado.meu);
        assertEquals(true,resultado.isCanEditOrDelete(byOrgaoJulgador));
        assertEquals(true,resultado.vara);
        assertEquals(true,resultado.meu);
    }

    @Test
    public void usuarioIsNull(){
        var resultado  = new ModeloMinuta("descricao",
            "template",
            "666",
            "64",
            null);
        assertEquals(null,resultado.getCpf());
        resultado.setUsuarioSaveUpdate(getUsuario());
        assertEquals("cpf",resultado.getCpf());
        assertEquals("cpf",resultado.getCpfAtualizacao());
    }

    @Test
    public void usuarioNotNullUsuarioAtualizacao(){
        var resultado  = new ModeloMinuta("descricao",
            "template",
            "666",
            "64",
            null);
        assertEquals(null,resultado.getCpf());
        resultado.setUsuarioSaveUpdate(getUsuario());
        assertEquals("cpf",resultado.getCpf());

        Usuario novoUsuario = new Usuario("windson", "usuarioNovo", List.of(), "token");
        resultado.setUsuarioSaveUpdate(novoUsuario);

        assertEquals("cpf",resultado.getCpf());
        assertEquals("usuarioNovo",resultado.getCpfAtualizacao());
    }
    @Test
    public void tagsDeveSerEmBranco() {
        var resultado = new ModeloMinuta("descricao",
            "template",
            "666",
            null,
            null);
        assertEquals(Collections.emptyList(), resultado.getTags());
    }

    private Usuario getUsuario(){
        OrgaoJulgador orgao = new OrgaoJulgador("PJGPG-666");
        Papel papel = new Papel("Assessor", List.of(orgao));
        Map<String, List<Papel>> mapPapeis = new HashMap<String, List<Papel>>();
        mapPapeis.put("PJEPG", List.of(papel));

        Usuario usuario = new Usuario("windson", "cpf", List.of(), "token");
        usuario.setPapeis(mapPapeis);
        return usuario;
    }
}
