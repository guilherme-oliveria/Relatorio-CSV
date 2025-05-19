package br.jus.tjro.gabinete.model.gab.enums;

public enum TipoPolo {

    A("Ativo"), P("Passivo"), T("Terceiro");

    private String label;

    TipoPolo() {
    }

    TipoPolo(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
