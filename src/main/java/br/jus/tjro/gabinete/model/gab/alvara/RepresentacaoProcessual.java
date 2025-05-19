package br.jus.tjro.gabinete.model.gab.alvara;

public enum RepresentacaoProcessual {

    ADVOGADO("1", "Advogado"),
    JUS_POSTULANDI("2", "Jus Postulandi");

    private final String valor;
    private final String descricao;

    RepresentacaoProcessual(String valor, String descricao) {
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
