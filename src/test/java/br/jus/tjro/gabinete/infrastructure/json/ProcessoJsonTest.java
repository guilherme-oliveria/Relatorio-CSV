package br.jus.tjro.gabinete.infrastructure.json;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcessoJsonTest {
    String json = "{\n" +
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
        "    \"fonteDados\": \"PJEPG\",\n" +
        "    \"manifestacao\": 0,\n" +
        "    \"idColegiado\": null" +
        "}";

    String json2 = "{\n" +
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
        "    \"fonteDados\": \"PJEPG\",\n" +
        "    \"manifestacao\": 0,\n" +
        "    \"idColegiado\": \"\"" +
        "}";

    String json3 = "{\n" +
        "\t\"id\": 411129,\n" +
        "\t\"idProcessoSistemaLegado\": 669742,\n" +
        "\t\"numeroProcesso\": \"7001311-76.2018.8.22.0004\",\n" +
        "\t\"orgaoJulgadorObj\": {\n" +
        "\t\t\"id\": \"PJEPG-129\",\n" +
        "\t\t\"descricao\": \"Ouro Preto do Oeste - 2ª Vara Cível\",\n" +
        "\t\t\"enderecos\": [\n" +
        "\t\t\t{\n" +
        "\t\t\t\t\"descricao\": \"Avenida Daniel Comboni\",\n" +
        "\t\t\t\t\"complemento\": \"(69)3416-1710 central_opo@tjro.jus.br\",\n" +
        "\t\t\t\t\"bairro\": \"União\",\n" +
        "\t\t\t\t\"municipio\": \"Ouro Preto do Oeste\",\n" +
        "\t\t\t\t\"numero\": \"1480\",\n" +
        "\t\t\t\t\"uf\": null,\n" +
        "\t\t\t\t\"cep\": \"76920-000\",\n" +
        "\t\t\t\t\"enderecoCompleto\": \"Avenida Daniel Comboni, nº 1480, Bairro União, CEP 76920-000, Ouro Preto do Oeste, (69)3416-1710 central_opo@tjro.jus.br\"\n" +
        "\t\t\t}\n" +
        "\t\t],\n" +
        "\t\t\"sigla\": \"OPO2CIV\",\n" +
        "\t\t\"idLegado\": \"129\",\n" +
        "\t\t\"sistema\": \"PJEPG\",\n" +
        "\t\t\"orgaosJulgadoresRevisores\": []\n" +
        "\t},\n" +
        "\t\"tpuClasse\": {\n" +
        "\t\t\"resourceName\": \"classes\",\n" +
        "\t\t\"codigo\": 156,\n" +
        "\t\t\"descricao\": \"Cumprimento de sentença\",\n" +
        "\t\t\"glossario\": \"\",\n" +
        "\t\t\"filhos\": [],\n" +
        "\t\t\"codigoPai\": null,\n" +
        "\t\t\"breadcrumb\": [\n" +
        "\t\t\t156\n" +
        "\t\t],\n" +
        "\t\t\"situacao\": \"A\",\n" +
        "\t\t\"temFilhos\": false\n" +
        "\t},\n" +
        "\t\"dataSincronizacao\": 1661231758417,\n" +
        "\t\"erroSincronizacao\": 0,\n" +
        "\t\"possuiLiminar\": true,\n" +
        "\t\"prioridade\": false,\n" +
        "\t\"segredoJustica\": false,\n" +
        "\t\"digital\": false,\n" +
        "\t\"justicaGratuita\": true,\n" +
        "\t\"sistema\": \"PJEPG\",\n" +
        "\t\"manifestacaoDescricao\": \"Despacho\",\n" +
        "\t\"dataUltimaDistribuicao\": 1522177333818,\n" +
        "\t\"valorCausa\": 11448,\n" +
        "\t\"assuntoPrincipal\": \"Restabelecimento\",\n" +
        "\t\"competencia\": \"Fazenda Pública\",\n" +
        "\t\"prioridades\": [],\n" +
        "\t\"processoAssuntos\": [\n" +
        "\t\t{\n" +
        "\t\t\t\"id\": 570355,\n" +
        "\t\t\t\"idAssunto\": 6095,\n" +
        "\t\t\t\"dataInclusao\": null,\n" +
        "\t\t\t\"dataExclusao\": null,\n" +
        "\t\t\t\"descricao\": null,\n" +
        "\t\t\t\"assuntoPrincipal\": false,\n" +
        "\t\t\t\"excluido\": null\n" +
        "\t\t},\n" +
        "\t\t{\n" +
        "\t\t\t\"id\": 570356,\n" +
        "\t\t\t\"idAssunto\": 6178,\n" +
        "\t\t\t\"dataInclusao\": null,\n" +
        "\t\t\t\"dataExclusao\": null,\n" +
        "\t\t\t\"descricao\": null,\n" +
        "\t\t\t\"assuntoPrincipal\": true,\n" +
        "\t\t\t\"excluido\": null\n" +
        "\t\t},\n" +
        "\t\t{\n" +
        "\t\t\t\"id\": 570357,\n" +
        "\t\t\t\"idAssunto\": 8961,\n" +
        "\t\t\t\"dataInclusao\": null,\n" +
        "\t\t\t\"dataExclusao\": null,\n" +
        "\t\t\t\"descricao\": null,\n" +
        "\t\t\t\"assuntoPrincipal\": false,\n" +
        "\t\t\t\"excluido\": null\n" +
        "\t\t}\n" +
        "\t],\n" +
        "\t\"nivelSigilo\": 0,\n" +
        "\t\"idColegiado\": null,\n" +
        "\t\"dataEntradaFormatada\": \"19 de ago. de 2022 16:55:00\",\n" +
        "\t\"partes\": [\n" +
        "\t\t{\n" +
        "\t\t\t\"id\": 1765237,\n" +
        "\t\t\t\"nome\": \"INSS - INSTITUTO NACIONAL DO SEGURO SOCIAL\",\n" +
        "\t\t\t\"tipoPolo\": \"Passivo\",\n" +
        "\t\t\t\"procuradoria\": \"PROCURADORIA FEDERAL EM RONDÔNIA\",\n" +
        "\t\t\t\"tipoParte\": \"REQUERIDO\",\n" +
        "\t\t\t\"pessoa\": {\n" +
        "\t\t\t\t\"nome\": \"INSS - INSTITUTO NACIONAL DO SEGURO SOCIAL\",\n" +
        "\t\t\t\t\"pessoaDocumentos\": [],\n" +
        "\t\t\t\t\"objetoUpdateString\": \"INSS - INSTITUTO NACIONAL DO SEGURO SOCIAL\"\n" +
        "\t\t\t}\n" +
        "\t\t},\n" +
        "\t\t{\n" +
        "\t\t\t\"id\": 1765238,\n" +
        "\t\t\t\"nome\": \"JHONATAN APARECIDO MAGRI\",\n" +
        "\t\t\t\"tipoPolo\": \"Ativo\",\n" +
        "\t\t\t\"procuradoria\": null,\n" +
        "\t\t\t\"tipoParte\": \"ADVOGADO\",\n" +
        "\t\t\t\"pessoa\": {\n" +
        "\t\t\t\t\"nome\": \"JHONATAN APARECIDO MAGRI\",\n" +
        "\t\t\t\t\"pessoaDocumentos\": [],\n" +
        "\t\t\t\t\"objetoUpdateString\": \"JHONATAN APARECIDO MAGRI\"\n" +
        "\t\t\t}\n" +
        "\t\t},\n" +
        "\t\t{\n" +
        "\t\t\t\"id\": 1765239,\n" +
        "\t\t\t\"nome\": \"DELICIO ALVES TEIXEIRA\",\n" +
        "\t\t\t\"tipoPolo\": \"Ativo\",\n" +
        "\t\t\t\"procuradoria\": null,\n" +
        "\t\t\t\"tipoParte\": \"ASSISTENTE DE ACUSAÇÃO\",\n" +
        "\t\t\t\"pessoa\": {\n" +
        "\t\t\t\t\"nome\": \"DELICIO ALVES TEIXEIRA\",\n" +
        "\t\t\t\t\"pessoaDocumentos\": [],\n" +
        "\t\t\t\t\"objetoUpdateString\": \"DELICIO ALVES TEIXEIRA\"\n" +
        "\t\t\t}\n" +
        "\t\t}\n" +
        "\t],\n" +
        "\t\"tarefa\": \"ParaIntegracao\",\n" +
        "\t\"orgaoJulgador\": \"PJEPG-129\",\n" +
        "\t\"fonteDados\": \"PJEPG\",\n" +
        "\t\"manifestacao\": 27,\n" +
        "\t\"tpuAssuntos\": [\n" +
        "\t\t{\n" +
        "\t\t\t\"resourceName\": \"assuntos\",\n" +
        "\t\t\t\"codigo\": 6095,\n" +
        "\t\t\t\"descricao\": \"Aposentadoria por Invalidez\",\n" +
        "\t\t\t\"glossario\": \"\",\n" +
        "\t\t\t\"filhos\": [],\n" +
        "\t\t\t\"codigoPai\": 6094,\n" +
        "\t\t\t\"breadcrumb\": [\n" +
        "\t\t\t\t6095\n" +
        "\t\t\t],\n" +
        "\t\t\t\"situacao\": \"A\",\n" +
        "\t\t\t\"temFilhos\": true\n" +
        "\t\t},\n" +
        "\t\t{\n" +
        "\t\t\t\"resourceName\": \"assuntos\",\n" +
        "\t\t\t\"codigo\": 6178,\n" +
        "\t\t\t\"descricao\": \"Restabelecimento\",\n" +
        "\t\t\t\"glossario\": \"\",\n" +
        "\t\t\t\"filhos\": [],\n" +
        "\t\t\t\"codigoPai\": 6173,\n" +
        "\t\t\t\"breadcrumb\": [\n" +
        "\t\t\t\t6178\n" +
        "\t\t\t],\n" +
        "\t\t\t\"situacao\": \"A\",\n" +
        "\t\t\t\"temFilhos\": false\n" +
        "\t\t},\n" +
        "\t\t{\n" +
        "\t\t\t\"resourceName\": \"assuntos\",\n" +
        "\t\t\t\"codigo\": 8961,\n" +
        "\t\t\t\"descricao\": \"Antecipação de Tutela / Tutela Específica\",\n" +
        "\t\t\t\"glossario\": \"\",\n" +
        "\t\t\t\"filhos\": [],\n" +
        "\t\t\t\"codigoPai\": 8960,\n" +
        "\t\t\t\"breadcrumb\": [\n" +
        "\t\t\t\t8961\n" +
        "\t\t\t],\n" +
        "\t\t\t\"situacao\": \"A\",\n" +
        "\t\t\t\"temFilhos\": true\n" +
        "\t\t}\n" +
        "\t],\n" +
        "\t\"idClasseJudicial\": 156\n" +
        "}";

    @Test
    public void deveDesserializarQuandoIdColegiadoForVazio() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Processo processo = mapper.reader().forType(Processo.class).readValue(json2);
        assertThat(processo.getIdColegiado()).isEmpty();
        assertEquals("424859", processo.getId().toString());
    }

    @Test
    public void deveDesserializarQuandoIdColegiadoForNull() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Processo processo = mapper.reader().forType(Processo.class).readValue(json);
        assertThat(processo.getIdColegiado()).isEmpty();
        assertEquals("424859", processo.getId().toString());
    }

    @Test
    public void deveSerializarProcesso() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Processo processo = mapper.reader().forType(Processo.class).readValue(json3);
        assertThat(processo.getIdColegiado()).isEmpty();
        assertEquals("411129", processo.getId().toString());

        String serializado = mapper.writeValueAsString(processo);
        assertThat(serializado).isNotBlank();
    }
}
