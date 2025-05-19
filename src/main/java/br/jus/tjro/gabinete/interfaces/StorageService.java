package br.jus.tjro.gabinete.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

public interface StorageService {

    String store(MultipartFile multipartFile) throws Exception;

    String store(byte[] bytes) throws Exception;

    InputStream loadInputStream(String hash) throws Exception;

    byte[] loadBytes(String hash) throws Exception;

    void delete(String hash) throws Exception;


}
