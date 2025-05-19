package br.jus.tjro.gabinete.service.local.localizador;

import br.jus.tjro.gabinete.UnitTest;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegraTest {
    @Test
    public void operadorIgualTrue() {
        Regra regra = new Regra();
        regra.setParametro("");
        regra.setOperador("=");
        regra.setValor("windson");
        assertEquals(true, regra.processa("windson"));
    }

    @Test
    public void operadorIgualFalse() {
        Regra regra = new Regra();
        regra.setOperador("=");
        regra.setValor("windson");
        assertEquals(false, regra.processa("stz"));
    }


    @Test
    public void operadorDiferenteTrue() {
        Regra regra = new Regra();
        regra.setOperador("!=");
        regra.setValor("windson");
        assertEquals(true, regra.processa("stz"));
    }

    @Test
    public void operadorDiferenteFalse() {
        Regra regra = new Regra();
        regra.setOperador("!=");
        regra.setValor("windson");
        assertEquals(false, regra.processa("windson"));
    }


    @Test
    public void operadorMaiorQueTrue() {
        Regra regra = new Regra();
        regra.setOperador(">");
        regra.setValor("400");
        assertEquals(true, regra.processa(700));
    }

    @Test
    public void operadorMaiorQueFalse() {
        Regra regra = new Regra();
        regra.setOperador(">");
        regra.setValor("400");
        assertEquals(false, regra.processa(200));
    }

    @Test
    public void operadorMenorQueTrue() {
        Regra regra = new Regra();
        regra.setOperador("<");
        regra.setValor("400");
        assertEquals(true, regra.processa(200));
    }

    @Test
    public void operadorMenorQueFalse() {
        Regra regra = new Regra();
        regra.setOperador("<");
        regra.setValor("400");
        assertEquals(false, regra.processa(400));
    }


    @Test
    public void operadorMaiorIgualQue() {
        Regra regra = new Regra();

        regra.setOperador("<=");
        regra.setValor("400");
        assertEquals(true, regra.processa(400));

        regra.setOperador("<=");
        regra.setValor("400");
        assertEquals(false, regra.processa(401));

        regra.setOperador("<=");
        regra.setValor("401");
        assertEquals(true, regra.processa(400));
    }

    @Test
    public void operadorMenorIgual() {
        Regra regra = new Regra();

        regra.setOperador(">=");
        regra.setValor("400");
        assertEquals(true, regra.processa(400));

        regra.setOperador(">=");
        regra.setValor("400");
        assertEquals(true, regra.processa(401));

        regra.setOperador(">=");
        regra.setValor("401");
        assertEquals(false, regra.processa(400));
    }

    @Test
    public void operadorContemTrue() {
        Regra regra = new Regra();

        regra.setOperador("%");
        regra.setValor("SANTO ANTÔNIO");
        assertEquals(true, regra.processa("santo antonio"));

        regra.setOperador("%");
        regra.setValor("SANTO ANTÔNIO");
        assertEquals(true, regra.processa("SANTO"));
    }

    @Test
    public void operadorContemFalse() {
        Regra regra = new Regra();

        regra.setOperador("%");
        regra.setValor("SANTO ANTÔNIO");
        assertEquals(false, regra.processa("ceron"));
    }

    @Test
    public void final7True() {
        Regra regra = new Regra();

        regra.setOperador("final");
        regra.setValor("7");
        assertEquals(true, regra.processa("7002167-49.2018.8.22.0001"));
    }

    @Test
    public void final7False() {
        Regra regra = new Regra();

        regra.setOperador("final");
        regra.setValor("7");
        assertEquals(false, regra.processa("7008436-41.2017.8.22.0001"));
    }

    @Test
    public void finalImparTrue() {
        Regra regra = new Regra();

        regra.setOperador("final");
        regra.setValor("impar");
        assertEquals(true, regra.processa("7002167-49.2018.8.22.0001"));
    }

    @Test
    public void finalImparFalse() {
        Regra regra = new Regra();

        regra.setOperador("final");
        regra.setValor("impar");
        assertEquals(false, regra.processa("7008436-41.2017.8.22.0001"));
    }

    @Test
    public void finalParTrue() {
        Regra regra = new Regra();

        regra.setOperador("final");
        regra.setValor("par");
        assertEquals(true, regra.processa("7008436-41.2017.8.22.0001"));
    }

    @Test
    public void finalParFalse() {
        Regra regra = new Regra();

        regra.setOperador("final");
        regra.setValor("par");
        assertEquals(false, regra.processa("7002167-49.2018.8.22.0001"));
    }

    @Test
    public void inicial7True() {
        Regra regra = new Regra();

        regra.setOperador("inicial");
        regra.setValor("7");
        assertEquals(true, regra.processa("7002167-49.2018.8.22.0001"));
    }

    @Test
    public void inicial7False() {
        Regra regra = new Regra();

        regra.setOperador("inicial");
        regra.setValor("7");
        assertEquals(false, regra.processa("6008436-41.2017.8.22.0001"));
    }

    @Test
    public void inicialImparTrue() {
        Regra regra = new Regra();

        regra.setOperador("inicial");
        regra.setValor("impar");
        assertEquals(true, regra.processa("7002167-49.2018.8.22.0001"));
    }

    @Test
    public void inicialImparFalse() {
        Regra regra = new Regra();

        regra.setOperador("inicial");
        regra.setValor("impar");
        assertEquals(false, regra.processa("6008436-41.2017.8.22.0001"));
    }

    @Test
    public void inicialParTrue() {
        Regra regra = new Regra();

        regra.setOperador("inicial");
        regra.setValor("par");
        assertEquals(true, regra.processa("6008436-41.2017.8.22.0001"));
    }

    @Test
    public void inicialParFalse() {
        Regra regra = new Regra();

        regra.setOperador("inicial");
        regra.setValor("par");
        assertEquals(false, regra.processa("7002167-49.2018.8.22.0001"));
    }

}

