package br.jus.tjro.gabinete.model.gab.alvara;

public enum OrigemDepositoEnum {

    CONTINGENCIA("CONTINGENCIA"),
    BACENJUD("BACENJUD"),
    TRIBUNAL("TRIBUNAL");

    private String descricao;

    OrigemDepositoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static OrigemDepositoEnum getEnum(String descricao) {

        if(descricao == null) {
            throw new IllegalArgumentException();
        }

        for(OrigemDepositoEnum v : values())
            if(descricao.equalsIgnoreCase(v.descricao)) return v;
        throw new IllegalArgumentException();
    }

}
