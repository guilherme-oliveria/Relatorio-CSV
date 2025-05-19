package br.jus.tjro.gabinete.config;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ColegiadoEnvironmentTests {

    @Test
    public void verificaValidacaoAntesEhDepoisInicilizacao(){
        assertFalse(ColegiadoEnvironment.isMinutaColegiado("minuta"));
        assertFalse(ColegiadoEnvironment.isAnexoColegiado("anexo"));
        assertFalse(ColegiadoEnvironment.isAnexoColegiado(null));
        //Inicializa listas
        new ColegiadoEnvironment(List.of("minuta"),List.of("anexo"), "");
        assertTrue(ColegiadoEnvironment.isMinutaColegiado("minuta"));
        assertTrue(ColegiadoEnvironment.isAnexoColegiado("anexo"));
        assertFalse(ColegiadoEnvironment.isAnexoColegiado(null));
    }
}
