package br.jus.tjro.gabinete.model.gab.enums;

public enum TipoComplementoEnum {

    TABELADO("Tabelado"), IDENTIFICADOR("Identificador"), LIVRE("Livre");

    public String tipo;

    TipoComplementoEnum(String tipo) {
        this.tipo = tipo;
    }

}
