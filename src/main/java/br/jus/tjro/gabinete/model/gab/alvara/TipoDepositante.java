package br.jus.tjro.gabinete.model.gab.alvara;

public enum TipoDepositante {
    AUTOR("1", "Autor"),
    REU("2", "Réu"),
    OUTROS("3", "Outros");

    private final String valor;
    private final String descricao;

    private TipoDepositante(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }

    public String getValor(){
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

}
