package br.jus.tjro.gabinete.infrastructure.json;

import br.jus.tjro.gabinete.model.gab.processo.ProcessoDocumento;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProcessoDocumentoJsonTest {


    @Test
    public void getUmDocumento() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ProcessoDocumento processoDocumento = mapper.reader().forType(ProcessoDocumento.class).readValue(json);
        assertEquals("FAGNER DA COSTA MENDES",processoDocumento.getNomeUserAssinatura());
    }

    @Test
    public void getDocumentoCompletoProxy() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ProcessoDocumento processoDocumento = mapper.reader().forType(ProcessoDocumento.class).readValue(jsonSlin);
        assertEquals("JOSE WILLYAN CAVALCANTE PINHEIRO",processoDocumento.getNomeUserAssinatura());
    }


    @Test
    public void jsonArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ProcessoDocumento[] processoDocumento = mapper.reader().readValue(jsonArray, ProcessoDocumento[].class);
        assertEquals(10,processoDocumento.length);
        assertTrue(Arrays.asList(processoDocumento).stream().noneMatch(it->it.getNomeUserAssinatura().equals("")));
    }

    String jsonSlin = "{\n" +
        "   \"id\":11160404,\n" +
        "   \"descricao\":\"0000226-55.2020.8.22.0021_VOL_001-3.pdf\",\n" +
        "   \"idProcesso\":214603,\n" +
        "   \"dataJuntada\":\"2020-12-08T16:57:33.755+0000\",\n" +
        "   \"nomeUserInclusao\":\"PJe - Usuário de Remessa\",\n" +
        "   \"extensao\":null,\n" +
        "   \"dsInstancia\":\"1\",\n" +
        "   \"ehSigiloso\":false,\n" +
        "   \"idUsuarioExclusao\":null,\n" +
        "   \"dataExclusao\":null,\n" +
        "   \"motivoExclusao\":null,\n" +
        "   \"ativo\":true,\n" +
        "   \"nrOrdem\":0,\n" +
        "   \"nomeUsuarioJuntada\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "   \"idStorage\":null,\n" +
        "   \"documentoHtml\":null,\n" +
        "   \"idDocumentoSistemaLegado\":11160404,\n" +
        "   \"nomeUserAssinatura\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "   \"nomeUltimaAssinatura\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "   \"hash\":null,\n" +
        "   \"tipoDocumento\":{\n" +
        "      \"descricao\":\"PETIÇÃO INICIAL\",\n" +
        "      \"ativo\":true,\n" +
        "      \"inPublico\":false,\n" +
        "      \"inTipoComunicacao\":null,\n" +
        "      \"inTipoExpediente\":null,\n" +
        "      \"tpVisibilidade\":\"A\",\n" +
        "      \"id\":12\n" +
        "   },\n" +
        "   \"dsTipoAnexo\":null\n" +
        "}";

    String json = "{\n" +
        "   \"id\":11160414,\n" +
        "   \"descricao\":\"OFICIO STJ\",\n" +
        "   \"documentoBin\":{\n" +
        "      \"idStorage\":\"4db2ec58a1916ba9c4def862b30ba7a766b70df2\",\n" +
        "      \"documentoHtml\":\"<p>ANEXO.</p>\",\n" +
        "      \"extensao\":\"text/html\",\n" +
        "      \"usuarioLogin\":{\n" +
        "         \"id\":null,\n" +
        "         \"nome\":\"PJe - Usuário de Remessa\",\n" +
        "         \"login\":\"20200121001\",\n" +
        "         \"email\":\"didesjud@tjro.jus.br\",\n" +
        "         \"in_tipo_pessoa\":\"F\",\n" +
        "         \"tipoPessoa\":{\n" +
        "            \"id_tipo_pessoa\":3,\n" +
        "            \"ds_tipo_pessoa\":\"Pessoa Física\",\n" +
        "            \"id_tipo_pessoa_superior\":null,\n" +
        "            \"in_ativo\":true\n" +
        "         },\n" +
        "         \"idPessoaLegado\":null,\n" +
        "         \"dataNascimento\":-2208973464000,\n" +
        "         \"dataObito\":null\n" +
        "      },\n" +
        "      \"assinaturas\":[\n" +
        "         {\n" +
        "            \"id\":10810204,\n" +
        "            \"dataAssinatura\":\"20/01/2021\",\n" +
        "            \"nomeSignatario\":\"FAGNER DA COSTA MENDES\",\n" +
        "            \"pessoa\":{\n" +
        "               \"id\":null,\n" +
        "               \"nome\":\"FAGNER DA COSTA MENDES\",\n" +
        "               \"login\":\"92049800282\",\n" +
        "               \"email\":\"fagner.mendes@tjro.jus.br\",\n" +
        "               \"in_tipo_pessoa\":\"F\",\n" +
        "               \"tipoPessoa\":{\n" +
        "                  \"id_tipo_pessoa\":3,\n" +
        "                  \"ds_tipo_pessoa\":\"Pessoa Física\",\n" +
        "                  \"id_tipo_pessoa_superior\":null,\n" +
        "                  \"in_ativo\":true\n" +
        "               },\n" +
        "               \"idPessoaLegado\":null,\n" +
        "               \"dataNascimento\":525931200000,\n" +
        "               \"dataObito\":null\n" +
        "            }\n" +
        "         }\n" +
        "      ],\n" +
        "      \"dsMd5Documento\":\"edbc22e38a3d1c7278f501ec9953844b\",\n" +
        "      \"documento\":null,\n" +
        "      \"binario\":false,\n" +
        "      \"anexo\":null,\n" +
        "      \"ultima_assinatura\":\"FAGNER DA COSTA MENDES\"\n" +
        "   },\n" +
        "   \"idProcesso\":214603,\n" +
        "   \"dataJuntada\":1611174260732,\n" +
        "   \"nomeUserInclusao\":\"PJe - Usuário de Remessa\",\n" +
        "   \"extensao\":\"text/html\",\n" +
        "   \"dsInstancia\":\"1\",\n" +
        "   \"ehSigiloso\":false,\n" +
        "   \"idUsuarioExclusao\":null,\n" +
        "   \"dataExclusao\":null,\n" +
        "   \"motivoExclusao\":null,\n" +
        "   \"ativo\":true,\n" +
        "   \"nrOrdem\":0,\n" +
        "   \"nomeUsuarioJuntada\":\"FAGNER DA COSTA MENDES\",\n" +
        "   \"idStorage\":\"4db2ec58a1916ba9c4def862b30ba7a766b70df2\",\n" +
        "   \"documentoHtml\":\"<p>ANEXO.</p>\",\n" +
        "   \"idDocumentoSistemaLegado\":11160414,\n" +
        "   \"nomeUserAssinatura\":\"FAGNER DA COSTA MENDES\",\n" +
        "   \"nomeUltimaAssinatura\":\"FAGNER DA COSTA MENDES\",\n" +
        "   \"hash\":null,\n" +
        "   \"tipoDocumento\":{\n" +
        "      \"descricao\":\"CERTIDÃO\",\n" +
        "      \"ativo\":true,\n" +
        "      \"inPublico\":false,\n" +
        "      \"inTipoComunicacao\":null,\n" +
        "      \"inTipoExpediente\":null,\n" +
        "      \"tpVisibilidade\":\"A\",\n" +
        "      \"id\":57\n" +
        "   },\n" +
        "   \"dsTipoAnexo\":null,\n" +
        "   \"usuarioLogin\":{\n" +
        "      \"id\":null,\n" +
        "      \"nome\":\"PJe - Usuário de Remessa\",\n" +
        "      \"login\":\"20200121001\",\n" +
        "      \"email\":\"didesjud@tjro.jus.br\",\n" +
        "      \"in_tipo_pessoa\":\"F\",\n" +
        "      \"tipoPessoa\":{\n" +
        "         \"id_tipo_pessoa\":3,\n" +
        "         \"ds_tipo_pessoa\":\"Pessoa Física\",\n" +
        "         \"id_tipo_pessoa_superior\":null,\n" +
        "         \"in_ativo\":true\n" +
        "      },\n" +
        "      \"idPessoaLegado\":null,\n" +
        "      \"dataNascimento\":-2208973464000,\n" +
        "      \"dataObito\":null\n" +
        "   },\n" +
        "   \"assinaturas\":[\n" +
        "      {\n" +
        "         \"id\":10810204,\n" +
        "         \"dataAssinatura\":\"20/01/2021\",\n" +
        "         \"nomeSignatario\":\"FAGNER DA COSTA MENDES\",\n" +
        "         \"pessoa\":{\n" +
        "            \"id\":null,\n" +
        "            \"nome\":\"FAGNER DA COSTA MENDES\",\n" +
        "            \"login\":\"92049800282\",\n" +
        "            \"email\":\"fagner.mendes@tjro.jus.br\",\n" +
        "            \"in_tipo_pessoa\":\"F\",\n" +
        "            \"tipoPessoa\":{\n" +
        "               \"id_tipo_pessoa\":3,\n" +
        "               \"ds_tipo_pessoa\":\"Pessoa Física\",\n" +
        "               \"id_tipo_pessoa_superior\":null,\n" +
        "               \"in_ativo\":true\n" +
        "            },\n" +
        "            \"idPessoaLegado\":null,\n" +
        "            \"dataNascimento\":525931200000,\n" +
        "            \"dataObito\":null\n" +
        "         }\n" +
        "      }\n" +
        "   ],\n" +
        "   \"dsMd5Documento\":\"edbc22e38a3d1c7278f501ec9953844b\",\n" +
        "   \"binario\":false,\n" +
        "   \"anexo\":null,\n" +
        "   \"ultima_assinatura\":\"FAGNER DA COSTA MENDES\"\n" +
        "}";

    String jsonArray = "[\n" +
        "      {\n" +
        "         \"id\":11160404,\n" +
        "         \"descricao\":\"0000226-55.2020.8.22.0021_VOL_001-3.pdf\",\n" +
        "         \"idProcesso\":214603,\n" +
        "         \"dataJuntada\":\"2020-12-08T16:57:33.755+0000\",\n" +
        "         \"nomeUserInclusao\":\"PJe - Usuário de Remessa\",\n" +
        "         \"extensao\":null,\n" +
        "         \"dsInstancia\":\"1\",\n" +
        "         \"ehSigiloso\":false,\n" +
        "         \"idUsuarioExclusao\":null,\n" +
        "         \"dataExclusao\":null,\n" +
        "         \"motivoExclusao\":null,\n" +
        "         \"ativo\":true,\n" +
        "         \"nrOrdem\":0,\n" +
        "         \"nomeUsuarioJuntada\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"idStorage\":null,\n" +
        "         \"documentoHtml\":null,\n" +
        "         \"idDocumentoSistemaLegado\":11160404,\n" +
        "         \"nomeUserAssinatura\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"nomeUltimaAssinatura\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"hash\":null,\n" +
        "         \"tipoDocumento\":{\n" +
        "            \"descricao\":\"PETIÇÃO INICIAL\",\n" +
        "            \"ativo\":true,\n" +
        "            \"inPublico\":false,\n" +
        "            \"inTipoComunicacao\":null,\n" +
        "            \"inTipoExpediente\":null,\n" +
        "            \"tpVisibilidade\":\"A\",\n" +
        "            \"id\":12\n" +
        "         },\n" +
        "         \"dsTipoAnexo\":null\n" +
        "      },\n" +
        "      {\n" +
        "         \"id\":11160405,\n" +
        "         \"descricao\":\"0000226-55.2020.8.22.0021_VOL_002-3.pdf\",\n" +
        "         \"idProcesso\":214603,\n" +
        "         \"dataJuntada\":\"2020-12-08T16:58:02.665+0000\",\n" +
        "         \"nomeUserInclusao\":\"PJe - Usuário de Remessa\",\n" +
        "         \"extensao\":null,\n" +
        "         \"dsInstancia\":\"1\",\n" +
        "         \"ehSigiloso\":false,\n" +
        "         \"idUsuarioExclusao\":null,\n" +
        "         \"dataExclusao\":null,\n" +
        "         \"motivoExclusao\":null,\n" +
        "         \"ativo\":true,\n" +
        "         \"nrOrdem\":0,\n" +
        "         \"nomeUsuarioJuntada\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"idStorage\":null,\n" +
        "         \"documentoHtml\":null,\n" +
        "         \"idDocumentoSistemaLegado\":11160405,\n" +
        "         \"nomeUserAssinatura\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"nomeUltimaAssinatura\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"hash\":null,\n" +
        "         \"tipoDocumento\":{\n" +
        "            \"descricao\":\"AUTOS DIGITALIZADOS\",\n" +
        "            \"ativo\":true,\n" +
        "            \"inPublico\":false,\n" +
        "            \"inTipoComunicacao\":null,\n" +
        "            \"inTipoExpediente\":null,\n" +
        "            \"tpVisibilidade\":\"A\",\n" +
        "            \"id\":161\n" +
        "         },\n" +
        "         \"dsTipoAnexo\":null\n" +
        "      },\n" +
        "      {\n" +
        "         \"id\":11160406,\n" +
        "         \"descricao\":\"0000226-55.2020.8.22.0021_VOL_003-3.pdf\",\n" +
        "         \"idProcesso\":214603,\n" +
        "         \"dataJuntada\":\"2020-12-08T16:58:17.678+0000\",\n" +
        "         \"nomeUserInclusao\":\"PJe - Usuário de Remessa\",\n" +
        "         \"extensao\":null,\n" +
        "         \"dsInstancia\":\"1\",\n" +
        "         \"ehSigiloso\":false,\n" +
        "         \"idUsuarioExclusao\":null,\n" +
        "         \"dataExclusao\":null,\n" +
        "         \"motivoExclusao\":null,\n" +
        "         \"ativo\":true,\n" +
        "         \"nrOrdem\":0,\n" +
        "         \"nomeUsuarioJuntada\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"idStorage\":null,\n" +
        "         \"documentoHtml\":null,\n" +
        "         \"idDocumentoSistemaLegado\":11160406,\n" +
        "         \"nomeUserAssinatura\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"nomeUltimaAssinatura\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"hash\":null,\n" +
        "         \"tipoDocumento\":{\n" +
        "            \"descricao\":\"AUTOS DIGITALIZADOS\",\n" +
        "            \"ativo\":true,\n" +
        "            \"inPublico\":false,\n" +
        "            \"inTipoComunicacao\":null,\n" +
        "            \"inTipoExpediente\":null,\n" +
        "            \"tpVisibilidade\":\"A\",\n" +
        "            \"id\":161\n" +
        "         },\n" +
        "         \"dsTipoAnexo\":null\n" +
        "      },\n" +
        "      {\n" +
        "         \"id\":11160407,\n" +
        "         \"descricao\":\"CERTIDÃO\",\n" +
        "         \"idProcesso\":214603,\n" +
        "         \"dataJuntada\":\"2020-12-09T22:10:26.607+0000\",\n" +
        "         \"nomeUserInclusao\":\"PJe - Usuário de Remessa\",\n" +
        "         \"extensao\":null,\n" +
        "         \"dsInstancia\":\"1\",\n" +
        "         \"ehSigiloso\":false,\n" +
        "         \"idUsuarioExclusao\":null,\n" +
        "         \"dataExclusao\":null,\n" +
        "         \"motivoExclusao\":null,\n" +
        "         \"ativo\":true,\n" +
        "         \"nrOrdem\":0,\n" +
        "         \"nomeUsuarioJuntada\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"idStorage\":null,\n" +
        "         \"documentoHtml\":null,\n" +
        "         \"idDocumentoSistemaLegado\":11160407,\n" +
        "         \"nomeUserAssinatura\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"nomeUltimaAssinatura\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"hash\":null,\n" +
        "         \"tipoDocumento\":{\n" +
        "            \"descricao\":\"CERTIDÃO\",\n" +
        "            \"ativo\":true,\n" +
        "            \"inPublico\":false,\n" +
        "            \"inTipoComunicacao\":null,\n" +
        "            \"inTipoExpediente\":null,\n" +
        "            \"tpVisibilidade\":\"A\",\n" +
        "            \"id\":57\n" +
        "         },\n" +
        "         \"dsTipoAnexo\":null\n" +
        "      },\n" +
        "      {\n" +
        "         \"id\":11160408,\n" +
        "         \"descricao\":\"INTIMAÇÃO\",\n" +
        "         \"idProcesso\":214603,\n" +
        "         \"dataJuntada\":\"2020-12-21T14:46:03.074+0000\",\n" +
        "         \"nomeUserInclusao\":\"PJe - Usuário de Remessa\",\n" +
        "         \"extensao\":null,\n" +
        "         \"dsInstancia\":\"1\",\n" +
        "         \"ehSigiloso\":false,\n" +
        "         \"idUsuarioExclusao\":null,\n" +
        "         \"dataExclusao\":null,\n" +
        "         \"motivoExclusao\":null,\n" +
        "         \"ativo\":true,\n" +
        "         \"nrOrdem\":0,\n" +
        "         \"nomeUsuarioJuntada\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"idStorage\":null,\n" +
        "         \"documentoHtml\":null,\n" +
        "         \"idDocumentoSistemaLegado\":11160408,\n" +
        "         \"nomeUserAssinatura\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"nomeUltimaAssinatura\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"hash\":null,\n" +
        "         \"tipoDocumento\":{\n" +
        "            \"descricao\":\"INTIMAÇÃO\",\n" +
        "            \"ativo\":true,\n" +
        "            \"inPublico\":false,\n" +
        "            \"inTipoComunicacao\":null,\n" +
        "            \"inTipoExpediente\":null,\n" +
        "            \"tpVisibilidade\":\"A\",\n" +
        "            \"id\":60\n" +
        "         },\n" +
        "         \"dsTipoAnexo\":null\n" +
        "      },\n" +
        "      {\n" +
        "         \"id\":11160409,\n" +
        "         \"descricao\":\"INTIMAÇÃO\",\n" +
        "         \"idProcesso\":214603,\n" +
        "         \"dataJuntada\":\"2020-12-21T14:46:03.188+0000\",\n" +
        "         \"nomeUserInclusao\":\"PJe - Usuário de Remessa\",\n" +
        "         \"extensao\":null,\n" +
        "         \"dsInstancia\":\"1\",\n" +
        "         \"ehSigiloso\":false,\n" +
        "         \"idUsuarioExclusao\":null,\n" +
        "         \"dataExclusao\":null,\n" +
        "         \"motivoExclusao\":null,\n" +
        "         \"ativo\":true,\n" +
        "         \"nrOrdem\":0,\n" +
        "         \"nomeUsuarioJuntada\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"idStorage\":null,\n" +
        "         \"documentoHtml\":null,\n" +
        "         \"idDocumentoSistemaLegado\":11160409,\n" +
        "         \"nomeUserAssinatura\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"nomeUltimaAssinatura\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"hash\":null,\n" +
        "         \"tipoDocumento\":{\n" +
        "            \"descricao\":\"INTIMAÇÃO\",\n" +
        "            \"ativo\":true,\n" +
        "            \"inPublico\":false,\n" +
        "            \"inTipoComunicacao\":null,\n" +
        "            \"inTipoExpediente\":null,\n" +
        "            \"tpVisibilidade\":\"A\",\n" +
        "            \"id\":60\n" +
        "         },\n" +
        "         \"dsTipoAnexo\":null\n" +
        "      },\n" +
        "      {\n" +
        "         \"id\":11160410,\n" +
        "         \"descricao\":\"Documento-MPRO-00002265520208220021.pdf\",\n" +
        "         \"idProcesso\":214603,\n" +
        "         \"dataJuntada\":\"2020-12-28T22:33:33.741+0000\",\n" +
        "         \"nomeUserInclusao\":\"PJe - Usuário de Remessa\",\n" +
        "         \"extensao\":null,\n" +
        "         \"dsInstancia\":\"1\",\n" +
        "         \"ehSigiloso\":false,\n" +
        "         \"idUsuarioExclusao\":null,\n" +
        "         \"dataExclusao\":null,\n" +
        "         \"motivoExclusao\":null,\n" +
        "         \"ativo\":true,\n" +
        "         \"nrOrdem\":0,\n" +
        "         \"nomeUsuarioJuntada\":\"ALUILDO DE OLIVEIRA LEITE\",\n" +
        "         \"idStorage\":null,\n" +
        "         \"documentoHtml\":null,\n" +
        "         \"idDocumentoSistemaLegado\":11160410,\n" +
        "         \"nomeUserAssinatura\":\"ALUILDO DE OLIVEIRA LEITE\",\n" +
        "         \"nomeUltimaAssinatura\":\"MATHEUS KUHN GONCALVES\",\n" +
        "         \"hash\":null,\n" +
        "         \"tipoDocumento\":{\n" +
        "            \"descricao\":\"MANIFESTAÇÃO\",\n" +
        "            \"ativo\":true,\n" +
        "            \"inPublico\":false,\n" +
        "            \"inTipoComunicacao\":null,\n" +
        "            \"inTipoExpediente\":null,\n" +
        "            \"tpVisibilidade\":\"A\",\n" +
        "            \"id\":32\n" +
        "         },\n" +
        "         \"dsTipoAnexo\":null\n" +
        "      },\n" +
        "      {\n" +
        "         \"id\":11160411,\n" +
        "         \"descricao\":\"DESPACHO\",\n" +
        "         \"idProcesso\":214603,\n" +
        "         \"dataJuntada\":\"2021-01-07T22:52:23.201+0000\",\n" +
        "         \"nomeUserInclusao\":\"PJe - Usuário de Remessa\",\n" +
        "         \"extensao\":null,\n" +
        "         \"dsInstancia\":\"1\",\n" +
        "         \"ehSigiloso\":false,\n" +
        "         \"idUsuarioExclusao\":null,\n" +
        "         \"dataExclusao\":null,\n" +
        "         \"motivoExclusao\":null,\n" +
        "         \"ativo\":true,\n" +
        "         \"nrOrdem\":0,\n" +
        "         \"nomeUsuarioJuntada\":\"HEDY CARLOS SOARES\",\n" +
        "         \"idStorage\":null,\n" +
        "         \"documentoHtml\":null,\n" +
        "         \"idDocumentoSistemaLegado\":11160411,\n" +
        "         \"nomeUserAssinatura\":\"HEDY CARLOS SOARES\",\n" +
        "         \"nomeUltimaAssinatura\":\"HEDY CARLOS SOARES\",\n" +
        "         \"hash\":null,\n" +
        "         \"tipoDocumento\":{\n" +
        "            \"descricao\":\"DESPACHO\",\n" +
        "            \"ativo\":true,\n" +
        "            \"inPublico\":false,\n" +
        "            \"inTipoComunicacao\":null,\n" +
        "            \"inTipoExpediente\":null,\n" +
        "            \"tpVisibilidade\":\"A\",\n" +
        "            \"id\":63\n" +
        "         },\n" +
        "         \"dsTipoAnexo\":null\n" +
        "      },\n" +
        "      {\n" +
        "         \"id\":11160413,\n" +
        "         \"descricao\":\"AR 0226\",\n" +
        "         \"idProcesso\":214603,\n" +
        "         \"dataJuntada\":\"2021-01-13T21:53:04.519+0000\",\n" +
        "         \"nomeUserInclusao\":\"PJe - Usuário de Remessa\",\n" +
        "         \"extensao\":null,\n" +
        "         \"dsInstancia\":\"1\",\n" +
        "         \"ehSigiloso\":false,\n" +
        "         \"idUsuarioExclusao\":null,\n" +
        "         \"dataExclusao\":null,\n" +
        "         \"motivoExclusao\":null,\n" +
        "         \"ativo\":true,\n" +
        "         \"nrOrdem\":2,\n" +
        "         \"nomeUsuarioJuntada\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"idStorage\":null,\n" +
        "         \"documentoHtml\":null,\n" +
        "         \"idDocumentoSistemaLegado\":11160413,\n" +
        "         \"nomeUserAssinatura\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"nomeUltimaAssinatura\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"hash\":null,\n" +
        "         \"tipoDocumento\":{\n" +
        "            \"descricao\":\"CERTIDÃO\",\n" +
        "            \"ativo\":true,\n" +
        "            \"inPublico\":false,\n" +
        "            \"inTipoComunicacao\":null,\n" +
        "            \"inTipoExpediente\":null,\n" +
        "            \"tpVisibilidade\":\"A\",\n" +
        "            \"id\":57\n" +
        "         },\n" +
        "         \"dsTipoAnexo\":null\n" +
        "      },\n" +
        "      {\n" +
        "         \"id\":11160412,\n" +
        "         \"descricao\":\"CARTA AR\",\n" +
        "         \"idProcesso\":214603,\n" +
        "         \"dataJuntada\":\"2021-01-13T21:53:04.519+0000\",\n" +
        "         \"nomeUserInclusao\":\"PJe - Usuário de Remessa\",\n" +
        "         \"extensao\":null,\n" +
        "         \"dsInstancia\":\"1\",\n" +
        "         \"ehSigiloso\":false,\n" +
        "         \"idUsuarioExclusao\":null,\n" +
        "         \"dataExclusao\":null,\n" +
        "         \"motivoExclusao\":null,\n" +
        "         \"ativo\":true,\n" +
        "         \"nrOrdem\":0,\n" +
        "         \"nomeUsuarioJuntada\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"idStorage\":null,\n" +
        "         \"documentoHtml\":null,\n" +
        "         \"idDocumentoSistemaLegado\":11160412,\n" +
        "         \"nomeUserAssinatura\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"nomeUltimaAssinatura\":\"JOSE WILLYAN CAVALCANTE PINHEIRO\",\n" +
        "         \"hash\":null,\n" +
        "         \"tipoDocumento\":{\n" +
        "            \"descricao\":\"CERTIDÃO\",\n" +
        "            \"ativo\":true,\n" +
        "            \"inPublico\":false,\n" +
        "            \"inTipoComunicacao\":null,\n" +
        "            \"inTipoExpediente\":null,\n" +
        "            \"tpVisibilidade\":\"A\",\n" +
        "            \"id\":57\n" +
        "         },\n" +
        "         \"dsTipoAnexo\":null\n" +
        "      }\n" +
        "   ]";
}
