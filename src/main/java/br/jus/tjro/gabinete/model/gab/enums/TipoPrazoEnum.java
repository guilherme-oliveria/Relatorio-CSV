package br.jus.tjro.gabinete.model.gab.enums;

public enum TipoPrazoEnum {
    A("anos"),
    M("meses"),
    D("dias") ,
    H("horas"),
    N("minutos") ,
    C("data certa") ,
    S("sem prazo");

    private final String label;

    TipoPrazoEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
