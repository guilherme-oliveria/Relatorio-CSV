package br.jus.tjro.gabinete.model.gab.enums;

public enum PerfilAcesso {

    ROLE_ADMINISTRADOR("Admin"), ROLE_ASSESSOR("Assessor"), ROLE_MAGISTRADO("Magistrado");

    private final String descricao;

    PerfilAcesso(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
