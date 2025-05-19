package br.jus.tjro.gabinete.model.gab.processo;

import br.jus.tjro.gabinete.UnitTest;
import br.jus.tjro.gabinete.model.gab.transiente.ProcessoConcluso;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WrapperBuscaProcessoTest {
    @Test
    public void deveImportarQuandoProcessoConclusoEstaPresente(){
        WrapperBuscaProcesso wrapper = new WrapperBuscaProcesso(new ProcessoConcluso());
        assertTrue(wrapper.isImportarSistemaLegado());
        assertNull(wrapper.getProcesso());
        assertNotNull(wrapper.getProcessoConcluso());
    }

    @Test
    public void naoDeveImportarQuandoProcessoEstaPresente(){
        WrapperBuscaProcesso wrapper = new WrapperBuscaProcesso(new Processo());
        assertFalse(wrapper.isImportarSistemaLegado());
        assertNotNull(wrapper.getProcesso());
        assertNull(wrapper.getProcessoConcluso());
    }

    @Test
    public void testaParametroNullUsuario() {
        assertThrows(IllegalArgumentException.class, () ->
            new WrapperBuscaProcesso(new ProcessoConcluso(), null)
        );
    }

    @Test
    public void testaParametroNullProcesso() {
        assertThrows(IllegalArgumentException.class, () -> {
            Processo p = null;
            WrapperBuscaProcesso wrapper = new WrapperBuscaProcesso(p);
            assertFalse(wrapper.isImportarSistemaLegado());
        });
    }

    @Test
    public void testaParametroProcessoConclusoNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            ProcessoConcluso p = null;
            WrapperBuscaProcesso wrapper = new WrapperBuscaProcesso(p);
            assertFalse(wrapper.isImportarSistemaLegado());
        });
    }

    @Test
    public void testaParametroProcessoConclusoNullComUsuario() {
        assertThrows(IllegalArgumentException.class, () -> {
            ProcessoConcluso p = null;
            WrapperBuscaProcesso wrapper = new WrapperBuscaProcesso(p, null);
            assertFalse(wrapper.isImportarSistemaLegado());
        });
    }
}
