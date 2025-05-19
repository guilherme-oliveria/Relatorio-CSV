package br.jus.tjro.gabinete.model.gab.enums;

public enum TipoDeLocalizadorEnum {
    Usuario("Usuario"), Sistema("Sistema"), OrgaoJulgador("OrgaoJulgador");


    public String tipoDeLocalizador;

    TipoDeLocalizadorEnum(String tipo) {

        this.tipoDeLocalizador = tipo;
    }

    public static String gerarJson(TipoDeLocalizadorEnum[] tipoDeLocalizador) {
        String json = "[";
        int tamanho = tipoDeLocalizador.length;
        for (int i = 0; i < tamanho; i++) {
            json += TipoDeLocalizadorEnum.values()[i].toJson();
            if (i + 1 < tamanho)
                json += ",";
        }
        json += "]";
        return json;
    }

    public static TipoDeLocalizadorEnum fromString(String text) {
        for (TipoDeLocalizadorEnum b : TipoDeLocalizadorEnum.values()) {
            if (b.tipoDeLocalizador.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

    public String getTipoDeLocalizador() {
        return tipoDeLocalizador;
    }

    public String toJson() {
        return "{\"" + this.name() + "\": \"" + this.tipoDeLocalizador + "\"}";
    }

}
