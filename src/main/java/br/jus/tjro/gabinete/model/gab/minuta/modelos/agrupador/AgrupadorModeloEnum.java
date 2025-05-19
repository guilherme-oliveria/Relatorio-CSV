package br.jus.tjro.gabinete.model.gab.minuta.modelos.agrupador;

public enum AgrupadorModeloEnum {

    MEUS("Meus Modelos"),
    VARA("Modelos da Vara"),
    TJ("Modelos do TJ"),
    TODASVARAS("Modelos das lotações");

    public final String descricao;

    AgrupadorModeloEnum(String desc) {
        this.descricao = desc;
    }

    public static AgrupadorModeloEnum getEnum(String descricao){
        if(descricao == null)
            throw new IllegalArgumentException();
        for(AgrupadorModeloEnum v : values())
            if(descricao.equalsIgnoreCase(v.descricao)) return v;
        throw new IllegalArgumentException();
    }
}
