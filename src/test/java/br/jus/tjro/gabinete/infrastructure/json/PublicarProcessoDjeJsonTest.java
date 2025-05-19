package br.jus.tjro.gabinete.infrastructure.json;

import br.jus.tjro.gabinete.model.gab.minuta.PublicarProcessoDJE;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PublicarProcessoDjeJsonTest {

    String jsonValid = "{" +
            "\"publicar\": \"true\"" +
            " ,\"prazo\": 10" +
        "}";

    String jsonValidIssue = "" +
        "{\n" +
        "  \"id\":666, \n" +
        "  \"prazo\": null, \n" +
        "  \"conteudo\": \"\", \n" +
        "  \"publicar\": false\n" +
        "}";

    String jsonNull = "{}";


    String jsonInvalid = "" +
        "{\n" +
        "  \"id\":666, \n" +
        "  \"prazo\": null, \n" +
        "  \"conteudo\": \"\", \n" +
        "  \"publicar\": true\n" +
        "}";

    @Test
    public void valid() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        PublicarProcessoDJE dje = mapper.reader().forType(PublicarProcessoDJE.class).readValue(jsonValid);
        assertEquals(10,dje.getPrazo());
    }


    @Test
    public void validIssue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        PublicarProcessoDJE dje = mapper.reader().forType(PublicarProcessoDJE.class).readValue(jsonValidIssue);
        assertEquals(0,dje.getPrazo());
    }

    @Test
    public void descerializaNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        PublicarProcessoDJE dje = mapper.reader().forType(PublicarProcessoDJE.class).readValue(jsonNull);
        assertEquals(0,dje.getPrazo());
    }


    @Test()
    public void descerializaInvalid() throws IOException {
        Assertions.assertThrows(Exception.class, () -> {
            ObjectMapper mapper = new ObjectMapper();
            PublicarProcessoDJE dje = mapper.reader().forType(PublicarProcessoDJE.class).readValue(jsonInvalid);
        });
    }




}
