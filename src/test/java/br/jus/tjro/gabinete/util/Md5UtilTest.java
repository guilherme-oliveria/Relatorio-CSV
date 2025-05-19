package br.jus.tjro.gabinete.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Md5UtilTest {
    @Test
    public void comparaMd5Byte() throws IOException, NoSuchAlgorithmException {
        String entrada = "rouver informatica";
        String retorno = Md5Util.getMd5(entrada.getBytes());
        assertEquals("756B2433D191C3598343EE0A4A888D77",retorno);
    }
}
