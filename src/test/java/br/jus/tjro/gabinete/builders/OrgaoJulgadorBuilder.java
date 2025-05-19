package br.jus.tjro.gabinete.builders;

import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;

import java.util.ArrayList;

public class OrgaoJulgadorBuilder {

    private Long orgaoJulgador;

    private OrgaoJulgadorBuilder() {
    }

    public static OrgaoJulgador umOrgaoJulgador() {
        return new OrgaoJulgador(
            "PJEPG-01",
            "Gabinete Windson",
            new ArrayList<>(),
            "SIGLA",
            new ArrayList<>());
    }
}
