package br.jus.tjro.gabinete.model.gab.minuta.modelos.agrupador;

import br.jus.tjro.gabinete.UnitTest;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.BuscaModelo;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TjTests {

    @Test
    public void verificaDadosObrigatorios() throws Exception {
        BuscaModelo modelo = new BuscaModelo("CPF666","666", List.of("666","333"));
        Tj agrupador = new Tj();
        HashMap<String, List<String>> resultado = agrupador.query(modelo);
        assertEquals("Modelos do TJ", resultado.get("label").get(0));
        assertNull(resultado.get("cpf"));
        assertEquals("gabinete", resultado.get("sistema").get(0));
        assertNull(resultado.get("idOrgaoJulgador"));
        assertNull(resultado.get("idTipoDocumento"));
    }
}
