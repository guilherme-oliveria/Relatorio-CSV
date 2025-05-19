package br.jus.tjro.gabinete.model.gab.alvara;

public enum TipoCredito {
    EM_ESPECIE("1", "Em Espécie"),
    EM_CONTA("2", "Em Conta");

    private final String valor;
    private final String descricao;

    TipoCredito(String valor, String descricao) {
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
