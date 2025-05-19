package br.jus.tjro.gabinete.service.remoto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StorageRemotoServiceTest {

    private final StorageRemotoService storage;

    public StorageRemotoServiceTest() throws IOException {
        RestTemplate mock = mock(RestTemplate.class);
        RequestEntity<?> request = any(RequestEntity.class);
        Class type = any();
        when(mock.exchange(request,type)).thenReturn(new ResponseEntity("windson", HttpStatus.OK));
        storage = new StorageRemotoService(mock, "windson");
    }

    @Test
    public void tipoDeArquivoNull() {
        byte[] file = new byte[0];
        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            storage.store(file);
        });

        assertEquals(thrown.getMessage(),"O tipo do arquivo application/octet-stream não é suportado");
    }

    @Test
    public void validaTipoPdf() throws Exception {
        storage.store(getFileToByte(root+"pdf"));
    }


    @Test
    public void validaTipoMp3() throws Exception {
        storage.store(getFileToByte(root+"mp3"));
    }

    private String root = "./src/test/java/br/jus/tjro/gabinete/service/remoto/anexos/";

    private byte[] getFileToByte(String path) throws IOException {
        File file = new File(path);
        // init array with file length
        byte[] bytesArray = new byte[(int) file.length()];

        FileInputStream fis = new FileInputStream(file);
        fis.read(bytesArray); // read file into bytes[]
        fis.close();
        return bytesArray;
    }
}
