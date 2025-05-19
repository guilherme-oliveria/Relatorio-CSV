package br.jus.tjro.gabinete.model.gab;

import br.jus.tjro.gabinete.UnitTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TipoDocumentoTest {

    private final String oneJson = "{\"id\":\"02.06\",\"descricao\":\"Comunicações\",\"ativo\":true,\"minuta\":false}";

    private final String comNull = "{\"id\":\"02.06\",\"descricao\":\"Comunicações\",\"ativo\":null,\"minuta\":null}";

    private final String ausente = "{\"id\":\"02.06\",\"descricao\":\"Comunicações\"}";

    @Test
    public void criaObjetosemId(){
        TipoDocumento tipoDocumento = new TipoDocumento(null);
        assertEquals(null,tipoDocumento.getId());
        assertEquals("Tipo documento sem descrição",tipoDocumento.getDescricao());
    }

    @Test
    public void descerializaJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TipoDocumento tipoDocumento = mapper.reader().forType(TipoDocumento.class).readValue(oneJson);
        assertEquals("02.06",tipoDocumento.getId());
        assertEquals("Comunicações",tipoDocumento.getDescricao());
    }

    @Test
    public void descerializaJsonComNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TipoDocumento tipoDocumento = mapper.reader().forType(TipoDocumento.class).readValue(comNull);
        assertEquals("02.06",tipoDocumento.getId());
        assertEquals("Comunicações",tipoDocumento.getDescricao());
    }

    @Test
    public void descerializaJsonComAusentel() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TipoDocumento tipoDocumento = mapper.reader().forType(TipoDocumento.class).readValue(ausente);
        assertEquals("02.06",tipoDocumento.getId());
        assertEquals("Comunicações",tipoDocumento.getDescricao());
    }
}
