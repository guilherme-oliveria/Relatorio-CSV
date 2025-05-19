package br.jus.tjro.gabinete.model.gab;

import br.jus.tjro.gabinete.UnitTest;
import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorCaixa;
import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorRegra;
import br.jus.tjro.gabinete.model.gab.localizador.Regra;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LocalizadorCaixaTest {

    @Test
    public void testaSeManifestacaoExisteMutiplas(){
        LocalizadorCaixa localizador = new LocalizadorCaixa();
        List<Regra> regras = List.of(
            new Regra("caixa.id", "=", "666"),
            new Regra("caixa.id", "=", "333"),
            new Regra("windson", "!=", "633"),
            new Regra("caixa.id", "=", "111"));
        localizador.regras.addAll(regras);
        assertFalse(localizador.isManifestacao(666l));
        assertFalse(localizador.isManifestacao(333l));
        assertFalse(localizador.isManifestacao(111l));
        assertFalse(localizador.isManifestacao(633l));
        assertTrue(localizador.isManifestacao(-1l));
    }

    @Test
    public void testaSeManifestacaoExisteSingle(){
        LocalizadorCaixa localizador = new LocalizadorCaixa();
        List<Regra> regras = List.of(
            new Regra("caixa.id", "=", "666"));
        localizador.regras.addAll(regras);
        assertTrue(localizador.isManifestacao(666l));
        assertFalse(localizador.isManifestacao(3l));
        assertFalse(localizador.isManifestacao(-1l));
    }

    @Test
    public void testaManifestacaoMenos1(){
        LocalizadorCaixa localizador = new LocalizadorCaixa();
        assertTrue(localizador.isManifestacao(-1l));
        assertFalse(localizador.isManifestacao(666l));
    }

    @Test
    public void testaSeManifestacaoExisteRegraDiferente(){
        LocalizadorCaixa localizador = new LocalizadorCaixa();
        List<Regra> regras = List.of(
            new Regra("caixa.id", "!=", "666"));
        localizador.regras.addAll(regras);
        assertFalse(localizador.isManifestacao(666l));
        assertFalse(localizador.isManifestacao(3l));
        assertTrue(localizador.isManifestacao(-1l));
    }

    @Test
    public void testaTarefaEhnumDiferente(){
        LocalizadorCaixa localizador = new LocalizadorCaixa();

        LocalizadorRegra localizadorRegra = new LocalizadorRegra();
        localizadorRegra.setParametro("[{\"parametro\":\"tags.tag.id\",\"operador\":\"=\",\"valor\":\"5662\"},{\"parametro\":\"tarefaEnum\",\"operador\":\"!=\",\"valor\":\"Assinar\"}]");

        localizador.setRegra(localizadorRegra);
        localizador.criaRegras();

        assertEquals(9,localizador.regras.size());
    }

    @Test
    public void testaTarefaEhtresDiferente(){
        LocalizadorCaixa localizador = new LocalizadorCaixa();

        LocalizadorRegra localizadorRegra = new LocalizadorRegra();
        localizadorRegra.setParametro("[" +
            "{\"parametro\":\"tags.tag.id\",\"operador\":\"=\",\"valor\":\"5662\"}," +
            "{\"parametro\":\"tarefaEnum\",\"operador\":\"!=\",\"valor\":\"Minutar\"}," +
            "{\"parametro\":\"tarefaEnum\",\"operador\":\"!=\",\"valor\":\"Corrigir\"}," +
            "{\"parametro\":\"tarefaEnum\",\"operador\":\"!=\",\"valor\":\"Assinar\"}" +
            "]");

        localizador.setRegra(localizadorRegra);
        localizador.criaRegras();

        assertEquals(11,localizador.regras.size());
    }
}
