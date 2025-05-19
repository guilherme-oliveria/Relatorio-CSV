package br.jus.tjro.gabinete.model.gab;

import br.jus.tjro.gabinete.UnitTest;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResponsePageTests {

    @Test
    public void ultimaPagina(){
        var paginas = new ResponsePage(new ArrayList(),5,10,4);
        assertTrue(paginas.isLast());
    }

    @Test
    public void primeiraPagina(){
        var paginas = new ResponsePage(new ArrayList(),5,10,0);
        assertFalse(paginas.isLast());
    }
}
