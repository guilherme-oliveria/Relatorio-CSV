package br.jus.tjro.gabinete.model.gab.processo;

import br.jus.tjro.gabinete.UnitTest;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ProcessoTagTests {
    @Test
    public void testaExclusaoProcessoTag() {
        ProcessoTag processoTag = new ProcessoTag();
        assertNull(processoTag.getDataExclusao());
        assertNull(processoTag.getUsuarioExclusao());
        processoTag.deletar("teste");
        assertNotNull(processoTag.getDataExclusao());
        assertNotNull(processoTag.getUsuarioExclusao());
    }

    @Test
    public void testaExclusaoProcessoTagNomeGrande() {
        ProcessoTag processoTag = new ProcessoTag();
        assertNull(processoTag.getDataExclusao());
        assertNull(processoTag.getUsuarioExclusao());
        processoTag.deletar("Windson procura 666 passaros. Porem o jeson procura 333 borboletas");
        assertNotNull(processoTag.getDataExclusao());
        assertNotNull(processoTag.getUsuarioExclusao());
    }
}

