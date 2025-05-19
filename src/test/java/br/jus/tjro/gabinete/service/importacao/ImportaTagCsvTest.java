package br.jus.tjro.gabinete.service.importacao;

import br.jus.tjro.gabinete.exceptions.CabecalhoInvalidoException;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
public class ImportaTagCsvTest {

    @Autowired
    private ImportaTagCsv importaTagCsv;

    @Test
    void lerArquivoClientes() throws Exception {

        var inputArquivo = lerArquivoDeTeste("tagCsv/exemplo-tag.csv");
        var tags = importaTagCsv.lerArquivo(inputArquivo);

        assertAll(
            () -> assertEquals(1, tags.size()),
            () -> assertEquals("5000035-51.2023.8.13.0220", tags.get(0).getNrProcesso()),
            () -> assertEquals("Tag Importe test", tags.get(0).getLisNomeTagSplit().get(0)),
            () -> assertEquals("#11B200", tags.get(0).getCor())
        );
        inputArquivo.close();
    }


    @Test
    void arquivoSemCabecalho() throws Exception {
        var inputArquivo = lerArquivoDeTeste("tagCsv/tagSemCabecalho.csv");
        var excecaoEsperada = assertThrows(CabecalhoInvalidoException.class,
            () -> importaTagCsv.lerArquivo(inputArquivo));
        assertEquals("outro nome aqui,Nome_Tag,Cor_hexadecimal,Aviso,Link,Apenas_Gabinete", excecaoEsperada.getMessage());
        inputArquivo.close();
    }

    private InputStream lerArquivoDeTeste(final String nomeArquivo) throws Exception {
        var pathArquivo = ClassLoader.getSystemResource(nomeArquivo).toURI().getPath();
        return new FileInputStream(pathArquivo);
    }
}
