package br.jus.tjro.gabinete.model.gab.minuta;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PublicarProcessoDJETest {
    @Test
    public void isPublicarPrazoMaiorZeroExeption() {
        assertThrows(Exception.class, () -> new PublicarProcessoDJE(null,null,"",null,true));
    }

    @Test
    public void isPublicarPrazoMaiorZero() throws Exception {
        PublicarProcessoDJE dje = new PublicarProcessoDJE(null, 10l, "", null, true);
        assertTrue(dje.getPrazo() > 0);
        assertTrue(dje.getPublicar());
    }

    @Test
    public void isNotPublicarPrazoEhZero() throws Exception {
        PublicarProcessoDJE publicar = new PublicarProcessoDJE(null, null, "", null, false);
        PublicarProcessoDJE publicarComPrazo = new PublicarProcessoDJE(null, 10l, "", null, false);
        assertEquals(0l, publicar.getPrazo().longValue());
        assertEquals(10l, publicarComPrazo.getPrazo().longValue());
    }
}
