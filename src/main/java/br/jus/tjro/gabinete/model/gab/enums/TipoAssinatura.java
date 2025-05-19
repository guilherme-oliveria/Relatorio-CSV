package br.jus.tjro.gabinete.model.gab.enums;

public enum TipoAssinatura {

    Cms("Cms"), Detached("Detached");

    public String descricao;

    TipoAssinatura(String desc) {
        this.descricao = desc;
    }

    public String getDescricao() {
        return this.descricao;
    }
}
