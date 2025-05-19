package br.jus.tjro.gabinete.model.gab.minuta.modelos;

import br.jus.tjro.gabinete.UnitTest;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BuscaModeloTest {

    @Test
    public void criaBuscaCpfNulo() {
        assertThrows(Exception.class, () -> new BuscaModelo(null,"lotacao", List.of("lotacao")));
    }

    @Test
    public void criaBuscaLotacaoAtualNulo() {
        assertThrows(Exception.class, () -> new BuscaModelo("018",null, List.of("lotacao")));
    }

    @Test
    public void criaBuscaLotacaoNulo() {
        assertThrows(Exception.class, () -> new BuscaModelo("018","lotacao", null));
    }

    @Test
    public void criaBuscaCompleto() throws Exception {
        BuscaModelo busca = new BuscaModelo("018",
            "lotacao1",
            List.of("lotacao1","lotacao2"),
            "String de pesquisa","tipoDocumento despacho");

        assertEquals("String de pesquisa",busca.getBusca().get("busca").stream().findFirst().orElseThrow());
        assertEquals("tipoDocumento despacho",busca.getIdTipoDocumento().get("idTipoDocumento").stream().findFirst().orElseThrow());
        assertEquals("lotacao1",busca.getLotacaoAtual().get("idOrgaoJulgador").stream().findFirst().orElseThrow());
        assertEquals(2,busca.getIdsLotacoes().get("idOrgaoJulgador").size());
        assertEquals("018",busca.getCpf().get("cpf").stream().findFirst().orElseThrow());
    }

    @Test
    public void criaBuscaMinimo() throws Exception {
        BuscaModelo busca = new BuscaModelo("018",
            "lotacao1",
            List.of("lotacao1","lotacao2"));

        assertEquals("018",busca.getCpf().get("cpf").stream().findFirst().orElseThrow());
        assertEquals("lotacao1",busca.getLotacaoAtual().get("idOrgaoJulgador").stream().findFirst().orElseThrow());
        assertEquals(2,busca.getIdsLotacoes().get("idOrgaoJulgador").size());
        assertNull(busca.getBusca());
        assertNull(busca.getIdTipoDocumento());
    }
}
