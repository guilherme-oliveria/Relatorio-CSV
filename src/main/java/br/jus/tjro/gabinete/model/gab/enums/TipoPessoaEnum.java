package br.jus.tjro.gabinete.model.gab.enums;

public enum TipoPessoaEnum {

    F("Física"), J("Jurídica"), A("Ente ou autoridade");

    private final String label;

    TipoPessoaEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
