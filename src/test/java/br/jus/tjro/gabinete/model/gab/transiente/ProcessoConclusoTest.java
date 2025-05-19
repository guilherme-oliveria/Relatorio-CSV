package br.jus.tjro.gabinete.model.gab.transiente;



import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProcessoConclusoTest {

    private final String json = "{\"idProcessoLegado\":75771,\"numeroProcesso\":\"0000019-69.2019.2.00.0000\",\"dataInicio\":1549646446917,\"instancia\":\"2\",\"justicaGratuita\":false,\"possuiLiminar\":false,\"segredoJustica\":false,\"orgaoJulgador\":11,\"classeJudicial\":\"1680\",\"valorCausa\":null,\"dataUltimaDistribuicao\":1549646534948,\"fonteDadosEnum\":\"PJEPG\"}";

    @Test
    public void processoConclusoDecerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        assertTrue(!json.contains("nivelSigilo"));
        ProcessoConcluso readValue = mapper.readValue(json, ProcessoConcluso.class);
        assertEquals(0, readValue.getNivelAcesso());
    }
}
