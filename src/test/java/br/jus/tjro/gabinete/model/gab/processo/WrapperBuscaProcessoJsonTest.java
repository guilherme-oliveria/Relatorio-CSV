package br.jus.tjro.gabinete.model.gab.processo;

import br.jus.tjro.gabinete.UnitTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WrapperBuscaProcessoJsonTest {

    String json = "{\"processoConcluso\":{\"idProcessoLegado\":846451,\"numeroProcesso\":\"7000398-69.2019.8.22.0001\",\"instancia\":\"1\",\"justicaGratuita\":true,\"possuiLiminar\":false,\"segredoJustica\":false,\"orgaoJulgador\":2,\"classeJudicial\":\"436\",\"caixa\":null,\"assuntoPrincipal\":null,\"dataUltimaDistribuicao\":1546907230571,\"valorCausa\":7586.89,\"fonteDadosEnum\":\"PJEPG\"},\"usuario\":{\"id\":6363,\"nome\":\"Andrew Ramires May\",\"permissoes\":[\"magistrado\",\"assessor\",\"admin\",\"assessor\"],\"token\":\"windson\"}}";

    @Test
    public void deveImportarQuandoProcessoConclusoEstaPresente() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        WrapperBuscaProcesso w = mapper.reader().forType(WrapperBuscaProcesso.class).readValue(json);
        assertEquals("846451",w.getProcessoConcluso().getIdProcessoLegado().toString());
    }
}
