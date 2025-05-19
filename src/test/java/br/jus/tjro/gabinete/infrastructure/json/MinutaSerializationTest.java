package br.jus.tjro.gabinete.infrastructure.json;

import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

public class MinutaSerializationTest {
    ObjectMapper mapper = new ObjectMapper();

    final String jsonReal = "{\"tipoDocumento\":{\"id\":\"64\",\"descricao\":\"DECISÃO\",\"ativo\":true,\"minuta\":true},\"covid\":false,\"idProcesso\":978335,\"minutaHtml\":\"<p>aa</p>\",\"minutaMovimentos\":[{\"id\":3584613,\"movimentoId\":12164,\"ordem\":0,\"descricao\":\"Outras Decisões\",\"minutaMovimentoComplementos\":[]}],\"anexos\":[{\"idMinutaPai\":2888691,\"statusUpload\":0}],\"versao\":1,\"autor\":\"0\",\"nomeAutor\":\"Andrew Ramires May\",\"publicacaoDje\":{\"id\":2357220,\"prazo\":15,\"conteudo\":\"\",\"publicar\":true},\"haOutrasComunicacoes\":false,\"emFaseFinalizacao\":false,\"id\":2888691,\"ehSigiloso\":null,\"arquivoAssinado\":null,\"questions\":[{\"value\":null,\"key\":\"UrgenteAndamento\",\"label\":\"Urgente\",\"required\":false,\"controlType\":\"checkbox\",\"type\":\"text\",\"toolTip\":\"\",\"options\":[],\"order\":1,\"persistence\":\"fluxo\"}],\"minutaHtmlRenderizadoEhAssinadoBytes\":null,\"ultimaAlteracao\":\"\"}";
    final String jsonRealComPropriedadeInvalidas = "{\"tipoDocumento\":{\"id\":\"64\",\"descricao\":\"DECISÃO\",\"ativo\":true,\"minuta\":true},\"covid\":false,\"idProcesso\":978335,\"minutaHtml\":\"<p>aa</p>\\n\",\"minutaMovimentos\":[{\"id\":3584613,\"movimentoId\":12164,\"ordem\":0,\"descricao\":\"Outras Decisões\",\"minutaMovimentoComplementos\":[]}],\"anexos\":[{\"idMinutaPai\":2888691,\"statusUpload\":0}],\"versao\":1,\"autor\":\"0\",\"nomeAutor\":\"Andrew Ramires May\",\"publicacaoDje\":{\"id\":2357220,\"prazo\":15,\"conteudo\":\"\",\"publicar\":true},\"haOutrasComunicacoes\":false,\"emFaseFinalizacao\":false,\"id\":2888691,\"ehSigiloso\":null,\"arquivoAssinado\":null,\"questions\":[{\"value\":null,\"key\":\"UrgenteAndamento\",\"label\":\"Urgente\",\"required\":false,\"controlType\":\"checkbox\",\"type\":\"text\",\"toolTip\":\"\",\"options\":[],\"order\":1,\"persistence\":\"fluxo\"}],\"minutaHtmlRenderizadoEhAssinadoBytes\":null,\"ultimaAlteracao\":\"\", \"hashP7s\": \"earthson\"}";
    final String jsonRealComHashDosAnexos = "{\"tipoDocumento\":{\"id\":\"64\",\"descricao\":\"DECISÃO\",\"ativo\":true,\"minuta\":true},\"covid\":false,\"idProcesso\":978335,\"minutaHtml\":\"<p>aa</p>\\n\",\"minutaMovimentos\":[{\"id\":3584613,\"movimentoId\":12164,\"ordem\":0,\"descricao\":\"Outras Decisões\",\"minutaMovimentoComplementos\":[]}],\"anexos\":[{\"hashP7s\": \"fireson\", \"hash\":\"waterson\",\"idMinutaPai\":2888691,\"statusUpload\":0}],\"versao\":1,\"autor\":\"0\",\"nomeAutor\":\"Andrew Ramires May\",\"publicacaoDje\":{\"id\":2357220,\"prazo\":15,\"conteudo\":\"\",\"publicar\":true},\"haOutrasComunicacoes\":false,\"emFaseFinalizacao\":false,\"id\":2888691,\"ehSigiloso\":null,\"arquivoAssinado\":null,\"questions\":[{\"value\":null,\"key\":\"UrgenteAndamento\",\"label\":\"Urgente\",\"required\":false,\"controlType\":\"checkbox\",\"type\":\"text\",\"toolTip\":\"\",\"options\":[],\"order\":1,\"persistence\":\"fluxo\"}],\"minutaHtmlRenderizadoEhAssinadoBytes\":null,\"ultimaAlteracao\":\"\", \"hashP7s\": \"earthson\"}";

    @Test
    public void testa_minuta_com_json_do_front_e_hash_p7s() throws JsonProcessingException {
        final var minuta = mapper.readValue(jsonRealComPropriedadeInvalidas, Minuta.class);
        assertEquals(minuta.getHashP7s(), null);
    }

    @Test
    public void testa_minuta_com_json_do_front() throws JsonProcessingException {
        final var minuta = mapper.readValue(jsonReal, Minuta.class);
        assertEquals( 978335,minuta.getProcesso().getId());
        assertEquals("<p>aa</p>", minuta.getMinutaHtml());
        assertNotNull(minuta.getPublicacaoDje());
        assertNotNull(minuta.getTipoDocumento());
    }

    @Test
    public void testa_minuta_com_json_do_front_com_hashes_dos_anexos() throws JsonProcessingException {
        final var minuta = mapper.readValue(jsonReal, Minuta.class);
        assertEquals(0,minuta.getAnexos().size());
    }

    @Test
    public void testa_minuta_com_deserializacao_de_anexos() throws Exception {
        final var minuta = new Minuta(1L);
        minuta.setAnexos(List.of(new MinutaAnexo(minuta, "allson", "html")));
        final var json = mapper.writeValueAsString(minuta);
        JSONAssert.assertEquals("{\"id\":1,\"anexos\":[{\"tipoDocumento\":null,\"id\":null,\"hash\":\"allson\",\"html\":null,\"descricao\":null,\"contentType\":\"html\",\"sigilo\":null,\"idMinutaSistemaLegado\":null,\"posicao\":null,\"minutaPaiTransient\":1,\"idMinutaPai\":1,\"extensao\":\".txt\"}],\"haOutrasComunicacoes\":false,\"emFaseFinalizacao\":false,\"covid\":false,\"questions\":[],\"ultimaAlteracao\":\"\"}", json,
                JSONCompareMode.LENIENT);
    }

    @Test
    public void testeValidation() throws JsonProcessingException {
        final var minuta = mapper.readValue("{\"idProcesso\": \"1\", \"anexos\": [{\"id\": \"1\", \"hash\":\"1234\"}]}", Minuta.class);
        assertEquals(minuta.getProcesso().getId(), 1);
    }
}

