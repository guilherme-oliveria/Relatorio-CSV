package br.jus.tjro.gabinete.service.remoto;

import br.jus.tjro.gabinete.exceptions.StorageException;
import br.jus.tjro.gabinete.interfaces.StorageService;
import org.apache.commons.codec.binary.Base64;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.util.Map;

import static java.util.Map.entry;


@Service
@ConditionalOnProperty(name="STORAGE_TYPE",matchIfMissing = true, havingValue="legado")
public class StorageRemotoService implements StorageService {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String user;
    private final String senha;

    @Autowired
    public StorageRemotoService(
        RestTemplate restTemplate,@Value("${STORAGE_URL:http://localhost:8033}") String baseUrl,
        @Value("${STORAGE_USER:}") String user,
        @Value("${STORAGE_SENHA:}") String senha){
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.user = user == null ? "" : user;
        this.senha = senha == null ? "" : senha;
    }

    private final Logger looger = LoggerFactory.getLogger(StorageRemotoService.class);

    public StorageRemotoService(RestTemplate restTemplate, String url) {
        this(restTemplate,url,"","");
    }

    @Override
    public String store(MultipartFile multipartFile) throws Exception {
        return store(multipartFile.getInputStream().readAllBytes());
    }

    @Override
    public String store(byte[] bytes) throws Exception {
        validaMimeTypeAndSize(bytes);
        String url = user.isEmpty() ? baseUrl+"/store-bytes" : baseUrl;
        var entity= RequestEntity.post(URI.create(url));
        addAuthorizationBasic(entity);
        return restTemplate.exchange(entity.body(bytes),String.class).getBody();
    }

    //TODO o nome do metodo nao explicita a decisão se adiciona ou nao credenciail
    private RequestEntity.HeadersBuilder addAuthorizationBasic(RequestEntity.HeadersBuilder builderEntri) {
        if(!user.isEmpty()){
            var base64 = new String(Base64.encodeBase64((user+":"+senha).getBytes()));
            builderEntri.header("Authorization", "Basic " + base64);
        }
        return builderEntri;
    }

    @Override
    public InputStream loadInputStream(String hash) throws StorageException {
        try {
            return new ByteArrayInputStream(loadBytes(hash));
        } catch (Exception e) {
            throw new StorageException("Erro ao carregar InputStream do hash "+hash,e);
        }
    }

    @Override
    public byte[] loadBytes(String hash) throws StorageException {
        try {
            String url = user.isEmpty() ? baseUrl+"/load-bytes" : baseUrl;
            var request = addAuthorizationBasic(RequestEntity.get(URI.create(url+"/"+hash))).build();
            return restTemplate.exchange(request, byte[].class).getBody();
        } catch (Exception e) {
            throw new StorageException("Erro ao carregar byte do hash "+hash,e);
        }
    }

    private void validaMimeTypeAndSize(byte[] file) throws Exception {
        String mimeType = new Tika().detect(file);
        float sizeInMb = (file.length/1024f)/1024f;
        if(mimeType == null)
            throw new Exception("Não foi possivel identificar o tipo do arquivo");
        if(!fileSize.containsKey(mimeType))
            throw new Exception("O tipo do arquivo "+mimeType+" não é suportado");
        if(sizeInMb > fileSize.get(mimeType))
            throw new Exception("O tamanho do arquivo não pode ser maior que "+sizeInMb+"MB");
    }


    private final Map<String,Float> fileSize = Map.ofEntries(
        entry("html", 1.5f),
        entry("image/png", 1.5f),
        entry("application/pdf", 50f),
        entry("ogv", 10f),
        entry("audio/vorbis", 10f),
        entry("mpeg", 50f),
        entry("oga", 10f),
        entry("png", 1.5f),
        entry("audio/mpeg", 50f),
        entry("audio/ogg", 10f),
        entry("audio/mp3", 10f),
        entry("pdf", 50f),
        entry("mpeg3", 10f),
        entry("text/html", 1.5f),
        entry("mp3", 10f),
        entry("mp4", 50f),
        entry("video/ogg", 10f),
        entry("audio/mpeg3", 10f),
        entry("video/mp4", 50f),
        entry("application/pkcs7-signature", 1f),
        entry("application/x-pkcs7-signature", 1f),
        entry("image/jpeg", 1f),
        entry("application/pkcs7-mime", 1f)
    );

    public void delete(String hash) throws StorageException {
        try {
            String url = baseUrl+"/"+hash;
            var request = addAuthorizationBasic(RequestEntity.delete(URI.create(url))).build();
            restTemplate.exchange(request, byte[].class).getBody();
        } catch (Exception e) {
            throw new StorageException("Erro deletar bytes "+hash,e);
        }
    }
}
