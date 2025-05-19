package br.jus.tjro.gabinete.infrastructure.json;

import br.jus.tjro.gabinete.model.gab.Audiencia;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AudienciaJsonTests {

    private final String json = "{\"status\":\"P\",\"linkVideoDrs\":\"http://aud.tjro.jus.br/?ProcessNumber=7011120-07.2015.8.22.0001&HearingDate=201802271200&AccessDate=202103241255&Hash=22CDF20F14F968BCFB069671EE10343A\",\"numProcesso\":\"7011120-07.2015.8.22.0001\",\"dataAudiencia\":\"2018-02-27 12:00:00\"}";

    @Test
    public void parse() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Audiencia resultado = mapper.reader().forType(Audiencia.class).readValue(json);
        assertEquals("P",resultado.getStatus());
    }

}
