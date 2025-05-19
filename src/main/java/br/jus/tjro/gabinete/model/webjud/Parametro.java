package br.jus.tjro.gabinete.model.webjud;

public class Parametro {

    private Long id;

    private String valor;

    public Parametro(String valor) {
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
