package br.jus.tjro.gabinete.listener.kafka.dtos;

import br.jus.tjro.gabinete.model.gab.enums.TipoVotoPreliminar;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoPautaDTOTest {

    @Test
    public void testeDeserializacao() throws JsonProcessingException {
        final List<PreliminarDTO> preliminares  = new ArrayList<>();
        final PreliminarDTO preliminar = new PreliminarDTO(55L, TipoVotoPreliminar.acolhida, 0, false);
        preliminares.add(preliminar);
        final PedidoPautaDTO pedidoPautaDTO = new PedidoPautaDTO(18L, "0801945-33.2031.8.32.0000",28L, "1", "PJESG-1", "11", preliminares);
        final ObjectMapper mapper = new ObjectMapper();
        final String json = mapper.writeValueAsString(pedidoPautaDTO);
        final String expected = """
            {
                "idProcesso":18,
                "numeroProcesso":"0801945-33.2031.8.32.0000",
                "idMinuta":28,
                "idOrgaoJulgador":"1",
                "idOrgaoJulgadorColegiado": "PJESG-1",
                "tipoVoto":"11",
                "preliminares": [{
                    "id": 55,
                    "tipoVoto": "acolhida",
                    "ordem": 0,
                    "prejudicaMerito": false
                }]
            }
            """.replaceAll("\\s+", "");
        assertEquals(expected, json, "Deve ser serializado corretamente");
    }


    @Test
    public void testeDeserializacaoSemPreliminar() throws JsonProcessingException {
        final List<PreliminarDTO> preliminares  = new ArrayList<>();
        final PedidoPautaDTO pedidoPautaDTO = new PedidoPautaDTO(18L,"0801945-33.2031.8.32.0000" ,28L, "1", "PJESG-1", "11", preliminares);
        final ObjectMapper mapper = new ObjectMapper();
        final String json = mapper.writeValueAsString(pedidoPautaDTO);
        final String expected = """
            {
                "idProcesso":18,
                "numeroProcesso":"0801945-33.2031.8.32.0000",
                "idMinuta":28,
                "idOrgaoJulgador":"1",
                "idOrgaoJulgadorColegiado": "PJESG-1",
                "tipoVoto":"11",
                "preliminares": []
            }
            """.replaceAll("\\s+", "");
        assertEquals(expected, json, "Deve ser serializado corretamente");
    }

}
