package br.jus.tjro.gabinete.service.local.assinatura;

import br.jus.tjro.assinaturadigital.utils.PKCS7Utils;
import br.jus.tjro.assinaturadigital.utils.vo.PKCS7;
import br.jus.tjro.gabinete.service.assinaturadigital.SignatarioValidacaoResultado;
import br.jus.tjro.gabinete.service.assinaturadigital.ValidadorAssinaturaDigitalService;
import br.jus.tjro.gabinete.util.ParametrosUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ValidacaoDeAssinaturaTest {

    private static PKCS7Utils pkcs7Util;

    @BeforeAll
    public static void setUp() {
        pkcs7Util = PKCS7Utils.getInstance();
    }

    private byte[] getFileToByte(String path) throws IOException {
        File file = new File(path);
        // init array with file length
        byte[] bytesArray = new byte[(int) file.length()];

        FileInputStream fis = new FileInputStream(file);
        fis.read(bytesArray); // read file into bytes[]
        fis.close();
        return bytesArray;
    }

    private byte[] getMsgOriginal() throws IOException {
        return getFileToByte("./src/test/java/br/jus/tjro/gabinete/service/local/assinatura/mensagem");
    }

    private byte[] getAssinatura() throws IOException {
        return getFileToByte("./src/test/java/br/jus/tjro/gabinete/service/local/assinatura/assinatura");
    }

    @Test
    public void converterParaAttachado() throws IOException {
        byte[] msgOrg = getMsgOriginal();
        byte[] assin = getAssinatura();
        pkcs7Util.converterParaAtachadoNovo(msgOrg, assin);
        assertEquals(true, true);
    }

    @Test
    public void realizaParseAssinatura() throws Exception {
        byte[] msgOrg = getMsgOriginal();
        byte[] assin = getAssinatura();
        byte[] msgCompleta = pkcs7Util.converterParaAtachadoNovo(msgOrg, assin);
        PKCS7 pkcs7Bytes = new PKCS7(msgCompleta);
        assertNotNull(pkcs7Bytes);
    }

    @Test
    public void validaSilenciosamente() throws Exception {
        ValidadorAssinaturaDigitalService validadorAssinaturaDigitalService = new ValidadorAssinaturaDigitalService(
            new ParametrosUtil(), new RestTemplate());
        byte[] msgOrg = getMsgOriginal();
        byte[] assin = getAssinatura();
        byte[] msgCompleta = pkcs7Util.converterParaAtachado(msgOrg, assin);
        PKCS7 pkcs7 = pkcs7Util.parse(msgCompleta);
        save(pkcs7.getBytesArquivoOriginal());
        List<SignatarioValidacaoResultado> resultados = validadorAssinaturaDigitalService.validarAssinaturaDigitalSilenciosamente(pkcs7.getSignedData());
        assertEquals(resultados.size(), 1);
        SignatarioValidacaoResultado resultado = resultados.stream().findFirst().orElse(null);
        assertEquals(
            resultado.getValidacaoCadeiaCertificado().getRequisicaoMensagem(),
            "Erro ao validar a cadeia de certificado pelo web service, mensagem interna: Cannot invoke \"br.jus.tjro.gabinete.service.local.ParametrosService.recuperarParametroPorConfiguracaoNome(String)\" because \"this.parametroService\" is null"
        );
    }

    private void save(byte[] bytesArquivoOriginal) {
        String dir = "./src/test/java/br/jus/tjro/gabinete/service/local/assinatura/mensagemAttachada";
    }
}
