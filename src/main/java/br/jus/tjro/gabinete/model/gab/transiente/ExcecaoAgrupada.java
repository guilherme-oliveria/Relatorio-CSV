package br.jus.tjro.gabinete.model.gab.transiente;


public class ExcecaoAgrupada {
    private String causa;
    private Long total;

    public ExcecaoAgrupada() {
    }

    public ExcecaoAgrupada(String causa, Long total) {
        this.causa = causa;
        this.total = total;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

}
