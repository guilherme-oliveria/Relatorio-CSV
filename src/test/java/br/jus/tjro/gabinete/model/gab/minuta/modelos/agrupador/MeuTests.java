package br.jus.tjro.gabinete.model.gab.minuta.modelos.agrupador;

import br.jus.tjro.gabinete.UnitTest;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.BuscaModelo;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MeuTests {

    @Test
    public void verificaDadosObrigatorios() throws Exception {
        BuscaModelo modelo = new BuscaModelo("CPF666","666", List.of("666","333"));
        Meu agrupador = new Meu();
        HashMap<String, List<String>> resultado = agrupador.query(modelo);
        assertEquals("Meus Modelos",resultado.get("label").get(0));
        assertEquals("CPF666",resultado.get("cpf").get(0));
        assertNull(resultado.get("lotacaoAtual"));
        assertNull(resultado.get("idTipoDocumento"));
    }
}
