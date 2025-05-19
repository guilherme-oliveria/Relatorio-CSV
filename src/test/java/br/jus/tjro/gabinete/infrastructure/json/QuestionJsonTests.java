package br.jus.tjro.gabinete.infrastructure.json;

import br.jus.tjro.gabinete.model.gab.question.QuestionBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionJsonTests {

    private final String json = "{\n" +
        "         \"value\":null,\n" +
        "         \"key\":\"fernanda\",\n" +
        "         \"label\":\"fernanda\",\n" +
        "         \"required\":false,\n" +
        "         \"order\":1,\n" +
        "         \"controlType\":\"textbox\",\n" +
        "         \"type\":\"text\",\n" +
        "         \"toolTip\":null,\n" +
        "         \"options\":null,\n" +
        "         \"keyAndValue\":null\n" +
        "      }";

    private final String jsonArray = "[\n" +
        "      {\n" +
        "         \"value\":null,\n" +
        "         \"key\":\"tipoDocumentoSelecionado\",\n" +
        "         \"label\":\"Windson Selecionado\",\n" +
        "         \"required\":false,\n" +
        "         \"order\":1,\n" +
        "         \"controlType\":\"dropdown\",\n" +
        "         \"type\":\"text\",\n" +
        "         \"toolTip\":null,\n" +
        "         \"options\":[\n" +
        "            {\n" +
        "               \"key\":\"windson\",\n" +
        "               \"value\":\"Windson Value\"\n" +
        "            }\n" +
        "         ],\n" +
        "         \"keyAndValue\":null\n" +
        "      },\n" +
        "      {\n" +
        "         \"value\":null,\n" +
        "         \"key\":\"fernanda\",\n" +
        "         \"label\":\"fernanda\",\n" +
        "         \"required\":false,\n" +
        "         \"order\":1,\n" +
        "         \"controlType\":\"textbox\",\n" +
        "         \"type\":\"text\",\n" +
        "         \"toolTip\":null,\n" +
        "         \"options\":null,\n" +
        "         \"keyAndValue\":null\n" +
        "      }\n" +
        "   ]";

    private final String jsonComValue = "{\n" +
        "         \"value\":\"TesteW\",\n" +
        "         \"key\":\"fernanda\",\n" +
        "         \"label\":\"fernanda\",\n" +
        "         \"required\":false,\n" +
        "         \"controlType\":\"textbox\",\n" +
        "         \"type\":\"text\",\n" +
        "         \"toolTip\":null,\n" +
        "         \"options\":[\n" +
        "            \n" +
        "         ],\n" +
        "         \"order\":1,\n" +
        "         \"keyAndValue\":{\n" +
        "            \"fernanda\":\"Teste\"\n" +
        "         }\n" +
        "      }";

    private final Set<QuestionBase> questionarios = new HashSet<>();
    private final String jsonNoBanco = "{\"chave\":\"valor\"}";


    @Test
    public void parse() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        QuestionBase resultado = mapper.reader().forType(QuestionBase.class).readValue(json);
        assertEquals("fernanda",resultado.getKey());
    }


    @Test
    public void parseParaArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        QuestionBase[] resultado = mapper.reader().readValue(jsonArray, QuestionBase[].class);
        assertEquals(2,resultado.length);
    }

    @Test
    public void praseComValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        QuestionBase resultado = mapper.reader().forType(QuestionBase.class).readValue(jsonComValue);
        assertEquals("fernanda",resultado.getKey());
        assertEquals("TesteW",resultado.getValue());
    }
}
