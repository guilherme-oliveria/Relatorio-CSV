package br.jus.tjro.gabinete.model;

import br.jus.tjro.gabinete.UnitTest;
import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorCaixa;
import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorRegra;
import br.jus.tjro.gabinete.model.gab.localizador.Regra;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrgaoJulgadorTests {

    @Test()
    public void temQueFalharPJEPG(){
        assertThrows(RuntimeException.class, () -> {new OrgaoJulgador("PJEPG-");});
    }

    @Test()
    public void temQueFalharPJESG(){
        assertThrows(RuntimeException.class, () -> {new OrgaoJulgador("PJESG-");});
    }
}
