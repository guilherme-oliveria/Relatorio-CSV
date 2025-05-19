package br.jus.tjro.gabinete.model.gab.alvara;

public enum TipoPessoa {
    PESSOA_FISICA("1", "Pessoa Física"),
    PESSOA_JURIDICA("2", "Pessoa Jurídica");

    private final String valor;
    private final String descricao;

    TipoPessoa(String valor, String descricao) {
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
