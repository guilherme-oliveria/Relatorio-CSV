package br.jus.tjro.gabinete.model.gab.enums;

public enum SistemaOrigemEnum {

    cartorio("cartorio");

    private final String descricao;

    SistemaOrigemEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
