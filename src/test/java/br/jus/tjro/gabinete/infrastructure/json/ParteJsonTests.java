package br.jus.tjro.gabinete.infrastructure.json;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParteJsonTests {


    @Test
    public void descerializaParte() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Processo processo = mapper.reader().forType(Processo.class).readValue(json);
        assertEquals(3,processo.getPartes().size());
    }



    String json = "{\n" +
        "  \"id\": 1901,\n" +
        "  \"idProcessoSistemaLegado\": 724444,\n" +
        "  \"numeroProcesso\": \"7023990-79.2018.8.22.0001\",\n" +
        "  \"orgaoJulgador\": 105,\n" +
        "  \"idClasseJudicial\": 98,\n" +
        "  \"dataSincronizacao\": 1533672054000,\n" +
        "  \"erroSincronizacao\": 0,\n" +
        "  \"possuiLiminar\": false,\n" +
        "  \"instancia\": 1,\n" +
        "  \"prioridade\": false,\n" +
        "  \"segredoJustica\": true,\n" +
        "  \"justicaGratuita\": true,\n" +
        "  \"sistema\": \"PJEPG\",\n" +
        "  \"manifestacaoDescricao\": \"Despacho Inicial\",\n" +
        "  \"dataUltimaDistribuicao\": 1529530140903,\n" +
        "  \"valorCausa\": 1000.0,\n" +
        "  \"assuntoPrincipal\": \"Dissolução\",\n" +
        "  \"prioridades\": [],\n" +
        "  \"tags\": [],\n" +
        "  \"processoAssuntos\": [\n" +
        "    {\n" +
        "      \"id\": 2021,\n" +
        "      \"idAssunto\": 7664,\n" +
        "      \"dataInclusao\": null,\n" +
        "      \"dataExclusao\": null,\n" +
        "      \"descricao\": null,\n" +
        "      \"assuntoPrincipal\": true,\n" +
        "      \"excluido\": null\n" +
        "    }\n" +
        "  ],\n" +
        "  \"dataEntradaFormatada\": \"20 de jun de 2018 17:28:32\",\n" +
        "  \"atualizaLocalizador\": true,\n" +
        "  \"tarefa\": \"NaoConcluso\",\n" +
        "  \"fonteDados\": \"PJEPG\",\n" +
        "  \"partes\": [\n" +
        "    {\n" +
        "      \"id\": 4981,\n" +
        "      \"nome\": \"DELNER DO CARMO AZEVEDO\",\n" +
        "      \"tipoPolo\": \"Ativo\",\n" +
        "      \"procuradoria\": null,\n" +
        "      \"tipoParte\": \"ADVOGADO\",\n" +
        "      \"pessoa\": {\n" +
        "        \"id\": null,\n" +
        "        \"idPessoaLegado\": null,\n" +
        "        \"nome\": \"DELNER DO CARMO AZEVEDO\",\n" +
        "        \"email\": null,\n" +
        "        \"dataNascimento\": null,\n" +
        "        \"dataObito\": null,\n" +
        "        \"pessoaDocumentos\": [],\n" +
        "        \"objetoUpdateString\": \"DELNER DO CARMO AZEVEDO\",\n" +
        "        \"objetoKeyString\": null,\n" +
        "        \"in_tipo_pessoa\": null\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 4982,\n" +
        "      \"nome\": \"DANIEL SOUZA DE OLIVEIRA\",\n" +
        "      \"tipoPolo\": \"Ativo\",\n" +
        "      \"procuradoria\": null,\n" +
        "      \"tipoParte\": \"REQUERENTE\",\n" +
        "      \"pessoa\": {\n" +
        "        \"id\": null,\n" +
        "        \"idPessoaLegado\": null,\n" +
        "        \"nome\": \"DANIEL SOUZA DE OLIVEIRA\",\n" +
        "        \"email\": null,\n" +
        "        \"dataNascimento\": null,\n" +
        "        \"dataObito\": null,\n" +
        "        \"pessoaDocumentos\": [],\n" +
        "        \"objetoUpdateString\": \"DANIEL SOUZA DE OLIVEIRA\",\n" +
        "        \"objetoKeyString\": null,\n" +
        "        \"in_tipo_pessoa\": null\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 4983,\n" +
        "      \"nome\": \"IVONETE COUTINHO MONTEIRO\",\n" +
        "      \"tipoPolo\": \"Ativo\",\n" +
        "      \"procuradoria\": null,\n" +
        "      \"tipoParte\": \"REQUERENTE\",\n" +
        "      \"pessoa\": {\n" +
        "        \"id\": null,\n" +
        "        \"idPessoaLegado\": null,\n" +
        "        \"nome\": \"IVONETE COUTINHO MONTEIRO\",\n" +
        "        \"email\": null,\n" +
        "        \"dataNascimento\": null,\n" +
        "        \"dataObito\": null,\n" +
        "        \"pessoaDocumentos\": [],\n" +
        "        \"objetoUpdateString\": \"IVONETE COUTINHO MONTEIRO\",\n" +
        "        \"objetoKeyString\": null,\n" +
        "        \"in_tipo_pessoa\": null\n" +
        "      }\n" +
        "    }\n" +
        "  ],\n" +
        "  \"manifestacao\": 82\n" +
        "}";
}
