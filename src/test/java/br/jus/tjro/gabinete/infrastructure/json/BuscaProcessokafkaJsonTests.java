package br.jus.tjro.gabinete.infrastructure.json;

import br.jus.tjro.gabinete.model.gab.processo.WrapperAtualizaProcesso;
import br.jus.tjro.gabinete.model.gab.processo.WrapperBuscaProcesso;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;


public class BuscaProcessokafkaJsonTests {


    @Test
    public void descerializa() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        WrapperBuscaProcesso wrapperProcesso = mapper.reader().forType(WrapperBuscaProcesso.class).readValue(json);
    }



    String json = "{\n" +
        "  \"processoConcluso\": {\n" +
        "    \"idProcessoLegado\": 1054062,\n" +
        "    \"numeroProcesso\": \"7007146-78.2019.8.22.0014\",\n" +
        "    \"instancia\": \"1\",\n" +
        "    \"justicaGratuita\": false,\n" +
        "    \"possuiLiminar\": false,\n" +
        "    \"segredoJustica\": false,\n" +
        "    \"digital\": false,\n" +
        "    \"orgaoJulgador\": \"136\",\n" +
        "    \"classeJudicial\": \"40\",\n" +
        "    \"assuntoPrincipal\": null,\n" +
        "    \"dataUltimaDistribuicao\": 1572296186788,\n" +
        "    \"valorCausa\": 2387.38,\n" +
        "    \"fonteDadosEnum\": \"PJEPG\",\n" +
        "    \"competencia\": \"Varas Cíveis\",\n" +
        "    \"listaIdsPrioridades\": null\n" +
        "  },\n" +
        "  \"usuario\": {\n" +
        "    \"nome\": \"Giulia Christinna Moura Dinon Paes Valadares\",\n" +
        "    \"id\": \"00996902252\",\n" +
        "    \"permissoes\": [\n" +
        "      \"assessor\"\n" +
        "    ],\n" +
        "    \"token\": \"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJHaXVsaWEgQ2hyaXN0aW5uYSBNb3VyYSBEaW5vbiBQYWVzIFZhbGFkYXJlcyIsInNldCI6IkdBQkRFUy1IU00gLSBHQUJJTkVURSBETyBERVNFTUJBUkdBRE9SIEhJUkFNIERFIFNPVVpBIE1BUlFVRVMiLCJlc3AiOiJBU1NFU1NPUiBERSBERVNFTUJBUkdBRE9SIiwiaXNzIjoiaHR0cHM6Ly9zb2wudGpyby5qdXMuYnIiLCJsb3QiOiJbNjcwNTBdIiwiY3BmIjoiMDA5OTY5MDIyNTIiLCJwZW0iOiJbYXNzZXNzb3JdIiwiaWQiOiI2MTMzIiwibGciOiI4MDQ5MjYiLCJleHAiOjE2MjAwNjQ2NjUsInBmcyI6Ils0NiwgNDAwLCA0NjEsIDU4MCwgODgwLCAxNDYxXSIsImlhdCI6MTYyMDA1NzQ2NSwiZnVuIjoibnVsbCJ9.Q83ck37RiYjW5ptb92zcXaYezKePtGRGU9cBJWFN3We9K2a5CrjL71Wu3B4C5Ctp-_A2e_Tzr-ADkUimolD0AA\",\n" +
        "    \"orgaosJulgadores\": [\n" +
        "      \"PJESG-12\"\n" +
        "    ],\n" +
        "    \"orgaosJulgadoresCompleto\": [\n" +
        "      {\n" +
        "        \"id\": \"PJESG-12\",\n" +
        "        \"descricao\": \"Gabinete Des. Hiram Souza Marques\",\n" +
        "        \"enderecos\": [],\n" +
        "        \"sigla\": \"GAB-SG-04\",\n" +
        "        \"idLegado\": \"12\",\n" +
        "        \"sistema\": \"PJESG\"\n" +
        "      }\n" +
        "    ],\n" +
        "    \"orgaosJulgadoresAsString\": [\n" +
        "      \"PJESG-12\"\n" +
        "    ]\n" +
        "  }\n" +
        "}";
}
