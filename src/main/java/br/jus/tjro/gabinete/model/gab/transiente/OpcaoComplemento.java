package br.jus.tjro.gabinete.model.gab.transiente;

public class OpcaoComplemento {
    private String codigo;
    private String descricao;

    public OpcaoComplemento() {

    }

    public OpcaoComplemento(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
