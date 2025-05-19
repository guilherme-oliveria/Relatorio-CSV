package br.jus.tjro.gabinete.service.assinaturadigital;

import br.jus.tjro.assinaturadigital.utils.CertificadoDigitalUtils;
import br.jus.tjro.validador.certificado.modelo.RespostaValidarCadeiaCertificado;

import java.security.cert.X509Certificate;

public class SignatarioValidacaoResultado {
    private boolean validaAssinaturaDigital;
    private RespostaValidarCadeiaCertificado validacaoCadeiaCertificado;
    private X509Certificate certificado;

    public SignatarioValidacaoResultado() {
    }

    public boolean isValidaAssinaturaDigital() {
        return validaAssinaturaDigital;
    }

    public void setValidaAssinaturaDigital(boolean validaAssinaturaDigital) {
        this.validaAssinaturaDigital = validaAssinaturaDigital;
    }

    public boolean isValidaCadeiaCertificado() {
        return getValidacaoCadeiaCertificado().isValidacaoSucesso();
    }

    public RespostaValidarCadeiaCertificado getValidacaoCadeiaCertificado() {
        return validacaoCadeiaCertificado;
    }

    public void setValidacaoCadeiaCertificado(RespostaValidarCadeiaCertificado validacaoCadeiaCertificado) {
        this.validacaoCadeiaCertificado = validacaoCadeiaCertificado;
    }

    public X509Certificate getCertificado() {
        return certificado;
    }

    public void setCertificado(X509Certificate certificado) {
        this.certificado = certificado;
    }

    public String getValidacaoErros() {

        StringBuilder resposta = new StringBuilder();

        if (!isValidaAssinaturaDigital()) {
            resposta.append(
                CertificadoDigitalUtils.getInstance().getSignatarioCN(getCertificado().getSubjectDN().getName()))
                .append(" = Assinatura digital inválida\n");
        }

        if (!isValidaCadeiaCertificado()) {
            resposta.append(
                ValidadorAssinaturaDigitalService.getValidacaoCadeiaCertificadoErros(getValidacaoCadeiaCertificado()));
        }

        return resposta.toString();
    }

}
