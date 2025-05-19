package br.jus.tjro.gabinete.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

public class PdfHelperTest {
    @Test
    public void converteHtmlParaPdf() throws Exception {
        String html = getHtml();
        byte[] retorno = PdfHelper.htmlToPdf(html, "http://localhost");
        if(retorno==null)
            fail("Falha ao converter html");
    }

    private byte[] getFileToByte(String path) throws IOException {
        File file = new File(path);
        // init array with file length
        byte[] bytesArray = new byte[(int) file.length()];

        FileInputStream fis = new FileInputStream(file);
        fis.read(bytesArray); // read file into bytes[]
        fis.close();
        return bytesArray;
    }

    private String getHtml() throws IOException {
        return new String(getFileToByte("./src/test/java/br/jus/tjro/gabinete/util/htmlToPdf"));
    }
}
