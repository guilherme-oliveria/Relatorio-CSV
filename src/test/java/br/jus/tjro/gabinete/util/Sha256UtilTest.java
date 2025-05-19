package br.jus.tjro.gabinete.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Sha256UtilTest {
    @Test
    public void comparaMd5Byte() throws IOException, NoSuchAlgorithmException {
        String entrada = "rouver informatica";
        String retorno = Sha256Util.getSha(entrada.getBytes());
        assertEquals("512DF3AB3FDCD5735CE71263636E695AD155F07AF1B227CCB563C056EB1A7B50",retorno);
    }
}
