package br.jus.tjro.gabinete.model.gab.assinaturamobile;

public class ResultadoAssinatura {
    private String assinatura;
    private String cadeiaCertificado;

    public ResultadoAssinatura() {
    }

    public String getAssinatura() {
        return this.assinatura;
    }

    public void setAssinatura(String assinatura) {
        this.assinatura = assinatura;
    }

    public String getCadeiaCertificado() {
        return this.cadeiaCertificado;
    }

    public void setCadeiaCertificado(String cadeiaCertificado) {
        this.cadeiaCertificado = cadeiaCertificado;
    }
}
