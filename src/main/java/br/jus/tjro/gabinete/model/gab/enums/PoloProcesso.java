package br.jus.tjro.gabinete.model.gab.enums;

public enum PoloProcesso {

    ATIVO("A"), PASSIVO("P");

    private final String descricao;

    PoloProcesso(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
