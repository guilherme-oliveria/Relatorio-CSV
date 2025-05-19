package br.jus.tjro.gabinete.model.gab.enums;

public enum TipoPublicidade {
    PUBLICO("Público"), PRIVADO("Privado");

    public String descricao;

    TipoPublicidade(String desc) {
        this.descricao = desc;
    }
}
