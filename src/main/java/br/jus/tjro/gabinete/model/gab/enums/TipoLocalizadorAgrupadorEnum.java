package br.jus.tjro.gabinete.model.gab.enums;

public enum TipoLocalizadorAgrupadorEnum {

    Manifestacao("Manifestacao"),
    Localizador("Localizador");

    public String tipoLocalizador;

    TipoLocalizadorAgrupadorEnum(String tipoLocalizador) {

        this.tipoLocalizador = tipoLocalizador;
    }

    public String getTipoLocalizador() {
        return tipoLocalizador;
    }

}
