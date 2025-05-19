package br.jus.tjro.gabinete.model.gab.minuta;

import br.jus.tjro.gabinete.UnitTest;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinutaMovimentoTest {

    @Test
    public void testaAdicaoDeminutaMovimento() {
        Minuta minuta = new Minuta();
        minuta.setMinutaMovimentos(Arrays.asList(new MinutaMovimento(minuta,5l)));
        assertEquals(1,minuta.getMinutaMovimentos().size());
    }

}
