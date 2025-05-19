package br.jus.tjro.gabinete.model.gab;

import br.jus.tjro.gabinete.UnitTest;
import br.jus.tjro.gabinete.model.gab.tag.Tag;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TagTests {

    @Test()
    public void DeveSerTagDeSistema() {
       Tag tag = new Tag("windson", "#windsblue", Tag.OrgaoTagTipoSistema);
       assertTrue(tag.isDeSistema());
    }

    @Test()
    public void NaooDeveSerTagDeSistema() {
        Tag tag = new Tag("windson", "#windsblue", "PJEPG-windson");
        assertFalse(tag.isDeSistema());
    }

    @Test()
    public void deSistemaEvitaNullPointer() {
        Tag tag = new Tag();
        assertFalse(tag.isDeSistema());
    }
}
