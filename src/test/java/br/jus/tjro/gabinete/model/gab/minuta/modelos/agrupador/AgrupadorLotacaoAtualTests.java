package br.jus.tjro.gabinete.model.gab.minuta.modelos.agrupador;

import br.jus.tjro.gabinete.UnitTest;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.BuscaModelo;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AgrupadorLotacaoAtualTests {

    @Test
    public void verificaDadosObrigatorios() throws Exception {
        BuscaModelo modelo = new BuscaModelo("CPF666","666", List.of("666","333"));
        AgrupadorLotacaoAtual agrupador = new AgrupadorLotacaoAtual();
        HashMap<String, List<String>> resultado = agrupador.query(modelo);
        assertEquals("Modelos da Vara",resultado.get("label").get(0));
        assertEquals(1,resultado.get("label").size());
        assertNull(resultado.get("cpf"));
        assertEquals(1,resultado.get("idOrgaoJulgador").size());
        assertEquals("666",resultado.get("idOrgaoJulgador").get(0));
        assertNull(resultado.get("idTipoDocumento"));
        assertEquals("gabinete",resultado.get("sistema").get(0));
    }

    @Test
    public void verificaAdicaoBusca() throws Exception {
        BuscaModelo modelo = new BuscaModelo("CPF666","666", List.of("666","333"),"busca");
        AgrupadorLotacaoAtual agrupador = new AgrupadorLotacaoAtual();
        HashMap<String, List<String>> resultado = agrupador.query(modelo);
        assertNull(resultado.get("cpf"));
        assertEquals(1,resultado.get("busca").size());
        assertEquals("busca",resultado.get("busca").get(0));
        assertNull(resultado.get("idTipoDocumento"));
    }
}
