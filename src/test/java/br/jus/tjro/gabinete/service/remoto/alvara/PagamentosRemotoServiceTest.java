package br.jus.tjro.gabinete.service.remoto.alvara;

import br.jus.tjro.gabinete.model.gab.alvara.Conta;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PagamentosRemotoServiceTest {

    @Test
    public void recupera_atraves_do_json() throws JsonProcessingException {
        String json = "{\"banco\":\"Default\",\"agencia\":\"0000\",\"operacao\":\"000.0\",\"numeroConta\":\"00000000\"," +
            "\"digitoVerificador\":\"0\",\"numeroProcesso\":\"00000000000000000\",\"idLegado\":0}";
        final Conta conta = new ObjectMapper().readValue(json, Conta.class);
        assertEquals(conta.getBanco(), "Default");
        assertEquals(conta.getIdLegado(), 0);
    }
}
