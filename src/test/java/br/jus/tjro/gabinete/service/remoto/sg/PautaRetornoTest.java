package br.jus.tjro.gabinete.service.remoto.sg;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PautaRetornoTest {
    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void recupera_atraves_do_json() throws JsonProcessingException {
        String json = "{\"idProcesso\":\"1\",\"documentos\":{\"1\":\"12\",\"2\":\"14\"}}";
        final PautaColegiadoRetorno pautaRetorno = mapper.readValue(json, PautaColegiadoRetorno.class);
        assertEquals(pautaRetorno.getLegado(1L), "12");
        assertEquals(pautaRetorno.getLegado(2L), "14");
        assertNull(pautaRetorno.getLegado(666L));
    }

}
