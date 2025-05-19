package br.jus.tjro.gabinete.model.gab.alvara;

public enum FinalidadePagamento {
    RECLAMANTE_AUTOR("01", "Reclamante/Autor"),
    RECLAMADO_REU("02", "Reclamado/Réu"),
    HONORARIOS_PERICIAIS("03", "Honorários Periciais"),
    HONORARIOS_ADVOCATICIOS("04", "Honorários Advocatícios"),
    LEILOEIRO("05", "Leiloeiro"),
    OUTROS("06", "Outros Interessados");

    private final String valor;
    private final String descricao;

    FinalidadePagamento(String valor, String descricao) {
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
