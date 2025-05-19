package br.jus.tjro.gabinete.repository.remoto;

import br.jus.tjro.gabinete.UnitTest;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryModeloDocumentoTests {
    @Test
    public void testaSeAdicionaSistemaNaQuery(){
        QueryModeloDocumento query = new QueryModeloDocumento(new HashMap<>());
        assertEquals("gabinete",query.getMap().get("sistema").get(0));
    }

    @Test
    public void testaSeMantemSistemaNaQuery(){
        QueryModeloDocumento query = new QueryModeloDocumento(new HashMap<>(Map.of("sistema", List.of("gabinete"))));
        assertEquals("gabinete",query.getMap().get("sistema").get(0));
    }
}
