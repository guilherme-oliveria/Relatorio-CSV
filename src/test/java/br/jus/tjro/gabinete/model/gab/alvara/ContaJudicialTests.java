package br.jus.tjro.gabinete.model.gab.alvara;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContaJudicialTests {
    @Test
    public void construcaoObjeto(){
        var test = new ContaJudicial("666",
            "2290-x", "6549832", "3", "104", "040",
            Double.valueOf("100"), "10/09/2019", Optional.of(OrigemDepositoEnum.BACENJUD));
        assertEquals("666",test.getCodLegado());
    }
}
