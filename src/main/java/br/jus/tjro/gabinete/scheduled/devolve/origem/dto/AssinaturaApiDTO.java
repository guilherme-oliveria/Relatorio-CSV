package br.jus.tjro.gabinete.scheduled.devolve.origem.dto;


public class AssinaturaApiDTO {
    private String cadeiaCertificado;
    private String cpfAssinou;
    private String dataAssinatura;
    private String assinatura;
    private String algoritmoDigest;

    public String getCadeiaCertificado() {
        return cadeiaCertificado;
    }

    public void setCadeiaCertificado(String cadeiaCertificado) {
        this.cadeiaCertificado = cadeiaCertificado;
    }

    public String getCpfAssinou() {
        return cpfAssinou;
    }

    public void setCpfAssinou(String cpfAssinou) {
        this.cpfAssinou = cpfAssinou;
    }

    public String getDataAssinatura() {
        return dataAssinatura;
    }

    public void setDataAssinatura(String dataAssinatura) {
        this.dataAssinatura = dataAssinatura;
    }

    public String getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(String assinatura) {
        this.assinatura = assinatura;
    }

    public String getAlgoritmoDigest() {
        return algoritmoDigest;
    }

    public void setAlgoritmoDigest(String algoritmoDigest) {
        this.algoritmoDigest = algoritmoDigest;
    }
}
