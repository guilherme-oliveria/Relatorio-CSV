package br.jus.tjro.gabinete.model.gab.enums;

public enum TipoVotoPreliminar {

    acolhida("acolhida"), rejeitada("rejeitada");

    private String label;

    TipoVotoPreliminar() {
    }

    TipoVotoPreliminar(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
