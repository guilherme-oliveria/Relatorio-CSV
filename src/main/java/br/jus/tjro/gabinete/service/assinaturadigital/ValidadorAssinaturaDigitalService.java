package br.jus.tjro.gabinete.service.assinaturadigital;

import br.jus.tjro.assinaturadigital.utils.CertificadoDigitalUtils;
import br.jus.tjro.gabinete.util.ParametrosUtil;
import br.jus.tjro.validador.certificado.modelo.CertificadoValidacaoResultado;
import br.jus.tjro.validador.certificado.modelo.RespostaValidarCadeiaCertificado;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationVerifier;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
public class ValidadorAssinaturaDigitalService {

    private final RestTemplate restTemplate;
    private final String validadorCertificadoPath = "validarCadeiaCertificado";
    private final ParametrosUtil parametro;

    @Autowired
    public ValidadorAssinaturaDigitalService(ParametrosUtil parametro, RestTemplate restTemplate) {
        this.parametro = parametro;
        if (Security.getProperty(BouncyCastleProvider.PROVIDER_NAME) == null)
            Security.addProvider(new BouncyCastleProvider());
        this.restTemplate = restTemplate;
    }

    public static String getValidacaoCadeiaCertificadoErros(RespostaValidarCadeiaCertificado resposta) {

        StringBuilder resultado = new StringBuilder();

        if (!resposta.isRequisicaoSucesso()) {
            resultado.append(resposta.getRequisicaoMensagem());
        } else {
            for (CertificadoValidacaoResultado certificadoValidacaoResultado : resposta
                .getCertificadosValidacoesResultados()) {
                if (!certificadoValidacaoResultado.isSucesso()) {
                    if (resultado.length() > 0) {
                        resultado.append("\n");
                    }
                    resultado.append(certificadoValidacaoResultado.getCertificadoCN());
                    resultado.append(" = [");
                    List<String> erros = certificadoValidacaoResultado.getErros();
                    for (int j = 0; j < erros.size(); j++) {
                        if (j > 0) {
                            resultado.append(", ");
                        }
                        resultado.append(erros.get(j));
                    }
                    resultado.append("]");
                }
            }
        }
        return resultado.toString();
    }

    public String getUrlValidador() {
        return parametro.getValorString("VALIDADOR_CERTIFICADO_PATH");
    }

    public void validarAssinaturaDigital(byte[] pkcs7Bytes) throws Exception {

        CMSSignedData signedData = new CMSSignedData(pkcs7Bytes);

        validarAssinaturaDigital(signedData);
    }

    public List<SignatarioValidacaoResultado> validarAssinaturaDigitalSilenciosamente(byte[] pkcs7Bytes)
        throws Exception {

        CMSSignedData signedData = new CMSSignedData(pkcs7Bytes);

        return validarAssinaturaDigitalSilenciosamente(signedData);
    }

    public void validarAssinaturaDigital(CMSSignedData signedData) throws Exception {

        List<SignatarioValidacaoResultado> resultados = validarAssinaturaDigitalSilenciosamente(signedData);

        StringBuilder msg = new StringBuilder();
        boolean valido = true;

        for (SignatarioValidacaoResultado signatarioResultado : resultados) {
            if (!signatarioResultado.isValidaAssinaturaDigital() || !signatarioResultado.isValidaCadeiaCertificado()) {
                valido = false;
                msg.append(signatarioResultado.getValidacaoErros());
            }
        }

        if (!valido) {
            throw new RuntimeException(msg.toString());
        }
    }

    @SuppressWarnings("unchecked")
    public List<SignatarioValidacaoResultado> validarAssinaturaDigitalSilenciosamente(CMSSignedData signedData)
        throws Exception {

        Store certStore = signedData.getCertificates();
        List<SignerInformation> signerInfos = (List<SignerInformation>) signedData.getSignerInfos().getSigners();

        if (signerInfos.size() <= 0) {
            throw new RuntimeException("O arquivo não possui nenhuma informação de assinatura!");
        }

        List<SignatarioValidacaoResultado> resultados = new ArrayList<SignatarioValidacaoResultado>();

        for (SignerInformation signerInfo : signerInfos) {

            Collection<X509CertificateHolder> certCollection = certStore.getMatches(signerInfo.getSID());

            X509Certificate[] certs = new X509Certificate[certCollection.size()];

            JcaX509CertificateConverter converter = new JcaX509CertificateConverter()
                .setProvider(BouncyCastleProvider.PROVIDER_NAME);

            int i = 0;

            for (X509CertificateHolder cert : certCollection) {
                certs[i++] = converter.getCertificate(cert);
            }

            SignatarioValidacaoResultado signatarioResultado = new SignatarioValidacaoResultado();

            SignerInformationVerifier verifier = new JcaSimpleSignerInfoVerifierBuilder()
                .setProvider(BouncyCastleProvider.PROVIDER_NAME).build(certs[0]);


            signatarioResultado.setCertificado(certs[0]);
            signatarioResultado.setValidaAssinaturaDigital(signerInfo.verify(verifier));
            signatarioResultado.setValidacaoCadeiaCertificado(validarCadeiaCertificado(certs));

            resultados.add(signatarioResultado);
        }

        return resultados;
    }

    private RespostaValidarCadeiaCertificado validarCadeiaCertificado(X509Certificate[] certs) {

        try {
            String cadeiaCertificado = CertificadoDigitalUtils.getInstance()
                .serializarCadeiaCertificadoComEncodingPkiPathEmBase64(certs);

            if (this.validadorCertificadoPath == null) {
                throw new RuntimeException(
                    "O endereço do serviço de validação de certificado não foi configurado, veja: AssinaturaDigitalUtils.inicializar(path)");
            }
            return fazRequisicaoParaServidorDeValidacao(cadeiaCertificado);
        } catch (Exception e) {
            RespostaValidarCadeiaCertificado resposta = new RespostaValidarCadeiaCertificado();
            resposta.setRequisicaoSucesso(false);
            resposta.setValidacaoSucesso(false);
            resposta.setRequisicaoMensagem(
                "Erro ao validar a cadeia de certificado pelo web service, mensagem interna: " + e.getMessage());
            return resposta;
        }
    }

    private RespostaValidarCadeiaCertificado fazRequisicaoParaServidorDeValidacao(String cadeiaCertificado) {
        URI uri = URI.create(getUrlValidador() + this.validadorCertificadoPath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("cadeiaCertificado", cadeiaCertificado);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        RespostaValidarCadeiaCertificado response = restTemplate
            .postForEntity(uri, request, RespostaValidarCadeiaCertificado.class).getBody();
        return response;
    }
}
