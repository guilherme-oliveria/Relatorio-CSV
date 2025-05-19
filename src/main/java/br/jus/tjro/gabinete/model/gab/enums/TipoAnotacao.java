package br.jus.tjro.gabinete.model.gab.enums;

public enum TipoAnotacao {

    Recusa("Recusa"), Informação("Informação");

    public String descricao;

    TipoAnotacao(String desc) {
        this.descricao = desc;
    }
}
