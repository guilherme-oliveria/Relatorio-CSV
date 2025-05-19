package br.jus.tjro.gabinete.repository;

import br.jus.tjro.gabinete.repository.gab.filter.ProcessoFilter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcessoFilterTests {
    @Test
    public void getClassJudicialNull(){
        ProcessoFilter filter = new ProcessoFilter();
        filter.getClasses();
        assertEquals(0,filter.getClasses().size());
    }


    @Test
    public void testa_is_empty(){
        ProcessoFilter filter = new ProcessoFilter();
        assertEquals(true,filter.isEmpty());
    }
}
