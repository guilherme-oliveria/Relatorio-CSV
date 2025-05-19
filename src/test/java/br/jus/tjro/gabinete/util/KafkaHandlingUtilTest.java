package br.jus.tjro.gabinete.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KafkaHandlingUtilTest {
    @Test
    public void comparaPrimeiraFalha() {
        String primeiraFalha = KafkaHandlingUtil.procuraProximoTopico("windson");
        assertEquals("windson_retry_5m", primeiraFalha);
    }

    @Test
    public void comparaSegundaFalha() {
        String segundaFalha = KafkaHandlingUtil.procuraProximoTopico("windson_retry_5m");
        assertEquals("windson_retry_30m", segundaFalha);
    }

    @Test
    public void comparaTerceiraFalha() {
        String segundaFalha = KafkaHandlingUtil.procuraProximoTopico("windson_retry_30m");
        assertEquals("windson_retry_1h", segundaFalha);
    }

    @Test
    public void comparaFalha() {
        String segundaFalha = KafkaHandlingUtil.procuraProximoTopico("windson_retry_1h");
        assertEquals("windson_fail", segundaFalha);
    }
}
