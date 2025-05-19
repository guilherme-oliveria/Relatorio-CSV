package br.jus.tjro.gabinete.regex;

import br.jus.tjro.gabinete.UnitTest;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RemoverCaracteresNumeroProcesso {

    @Test
    public void removePontosEhTracos(){
        assertEquals("666","6-6.6".replaceAll("[^0-9]",""));
    }

    @Test
    public void removePontosEhTracosDorados(){
        assertEquals("6666","6-6.6.6".replaceAll("[^0-9]",""));
    }

    @Test
    public void removePontosEhTracosNumeroCompleto(){
        assertEquals("70000442620198220007","7000044-26.2019.8.22.0007".replaceAll("[^0-9]",""));
    }
}
