package br.jus.tjro.gabinete.model.gab.transiente.VariavelTemplate;

public class OpcaoVariavelTemplate {
    private String valor;
    private String descricao;
    private Boolean padrao;

    public OpcaoVariavelTemplate() {

    }

    public OpcaoVariavelTemplate(String descricao, String valor, boolean padrao) {
        this.valor = valor;
        this.descricao = descricao;
        this.padrao = padrao;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getPadrao() {
        return padrao;
    }

    public void setPadrao(Boolean padrao) {
        this.padrao = padrao;
    }
}
