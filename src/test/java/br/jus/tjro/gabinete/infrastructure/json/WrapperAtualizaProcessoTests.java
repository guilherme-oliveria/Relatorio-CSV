package br.jus.tjro.gabinete.infrastructure.json;

import br.jus.tjro.gabinete.model.gab.processo.WrapperAtualizaProcesso;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class WrapperAtualizaProcessoTests {

    @Test
    public void descerializaJson2() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        WrapperAtualizaProcesso wrapperProcesso = mapper.reader().forType(WrapperAtualizaProcesso.class).readValue(json);
    }

    String json = "{\"idProcesso\":901813,\"usuario\":{\"nome\":\"Sistema\",\"id\":\"-1\",\"permissoes\":[\"SISTEMA\"],\"orgaosJulgadoresAsString\":[]}}";
}
