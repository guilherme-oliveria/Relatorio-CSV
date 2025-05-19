package br.jus.tjro.gabinete.infrastructure.json;

import br.jus.tjro.gabinete.model.gab.ProcessoRequestPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcessoRequestPageTests {


    String json = "{\n" +
        "  \"processo\": {\n" +
        "    \"id\": 424859,\n" +
        "    \"idProcessoSistemaLegado\": 1041939,\n" +
        "    \"dataSincronizacao\": 1581375500105,\n" +
        "    \"erroSincronizacao\": 0,\n" +
        "    \"possuiLiminar\": false,\n" +
        "    \"instancia\": 0,\n" +
        "    \"prioridade\": false,\n" +
        "    \"segredoJustica\": false,\n" +
        "    \"justicaGratuita\": false,\n" +
        "    \"sistema\": \"PJEPG\",\n" +
        "    \"manifestacaoDescricao\": \"\",\n" +
        "    \"partes\": [],\n" +
        "    \"idColegiado\": null,\n" +
        "    \"fonteDados\": \"PJEPG\",\n" +
        "    \"manifestacao\": 0\n" +
        "  },\n" +
        "  \"number\": 1\n" +
        "}";

    @Test
    public void desserialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ProcessoRequestPage processoRequestPage = mapper.reader().forType(ProcessoRequestPage.class).readValue(json);
        assertEquals("424859",processoRequestPage.getProcesso().getId().toString());
    }

    @Test
    public void serialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ProcessoRequestPage processoRequestPage = mapper.reader().forType(ProcessoRequestPage.class).readValue(json);
        assertEquals("424859",processoRequestPage.getProcesso().getId().toString());

        String retorno = mapper.writeValueAsString(processoRequestPage);
    }
}
