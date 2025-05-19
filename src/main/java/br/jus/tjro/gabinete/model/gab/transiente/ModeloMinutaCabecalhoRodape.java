package br.jus.tjro.gabinete.model.gab.transiente;

public class ModeloMinutaCabecalhoRodape {
    private Long idOrgaoJulgador;
    private String cabecalho;
    private String rodape;
    public ModeloMinutaCabecalhoRodape() {
    }

    public Long getIdOrgaoJulgador() {
        return idOrgaoJulgador;
    }

    public void setIdOrgaoJulgador(Long idOrgaoJulgador) {
        this.idOrgaoJulgador = idOrgaoJulgador;
    }

    public String getCabecalho() {
        return cabecalho;
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    public String getRodape() {
        return rodape;
    }

    public void setRodape(String rodape) {
        this.rodape = rodape;
    }
}
