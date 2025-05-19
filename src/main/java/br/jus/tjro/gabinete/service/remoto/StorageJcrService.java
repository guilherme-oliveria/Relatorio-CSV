package br.jus.tjro.gabinete.service.remoto;

import br.jus.tjro.gabinete.exceptions.StorageException;
import br.jus.tjro.gabinete.interfaces.StorageService;
import br.jus.tjro.gabinete.util.jcr.Connection;
import br.jus.tjro.gabinete.util.jcr.HashProvider;
import br.jus.tjro.gabinete.util.jcr.Range;
import br.jus.tjro.gabinete.util.jcr.ResourceUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.util.*;

import static java.util.Map.entry;


@Service
@ConditionalOnProperty(name="STORAGE_TYPE",matchIfMissing = false, havingValue="jcr")
public class StorageJcrService implements StorageService {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String user;
    private final String senha;

    private static Set<String> hashes = Collections.synchronizedSet(new LinkedHashSet());

    @Autowired
    public StorageJcrService(
        RestTemplate restTemplate,@Value("${STORAGE_URL:http://localhost:8033}") String baseUrl,
        @Value("${STORAGE_USER:}") String user,
        @Value("${STORAGE_SENHA:}") String senha){
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.user = user;
        this.senha = senha;
    }

    private final Logger looger = LoggerFactory.getLogger(StorageJcrService.class);

    public StorageJcrService(RestTemplate restTemplate, String url) {
        this(restTemplate,url,"","");
    }

    @Override
    public String store(MultipartFile multipartFile) throws Exception {
        return this.persistJcr(multipartFile.getInputStream());
    }

    @Override
    public String store(byte[] bytes) throws Exception {
        validaMimeTypeAndSize(bytes);
        var base64 = new String(Base64.encodeBase64((user+":"+senha).getBytes()));
        var entity = RequestEntity.put(URI.create(baseUrl)).header("Authorization", "Basic " + base64).body(bytes);
        return restTemplate.exchange(entity,String.class).getBody();
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
            var request = addAuthorizationBasic(RequestEntity.get(URI.create(baseUrl+"/"+hash))).build();
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

    private String persistJcr(InputStream data) throws StorageException {
        File temp = null;
        String hash = null;
        try {
            temp = this.createFile(data);
            hash = HashProvider.hash(temp);
            String uri = String.format("%s/%s", baseUrl, hash);
            if (!hashes.add(hash)) {
                while(hashes.contains(hash)) {
                    Thread.sleep(1000L);
                }
            }

            long fileSize = temp.length();
            long remoteSize = ResourceUtils.getResourceSize(uri, this.getHttpClient());
            long startPos = remoteSize > 0L ? remoteSize : 0L;
            int chunkSize = 1048576;
            if (fileSize > remoteSize) {
                InputStream stream = this.createStreamAfterPos(temp, startPos);
                byte[] buffer = new byte[chunkSize];

                try {
                    for(long pos = startPos; pos < fileSize; pos += (long)chunkSize) {
                        int read = stream.read(buffer);
                        int resp = ResourceUtils.putResource(uri, buffer, new Range(pos, read, fileSize), null, this.getHttpClient());
                        if (resp != 204) {
                            throw new StorageException("Falha ao enviar documento. Retorno inválido do servidor");
                        }
                    }
                } finally {
                    if (stream != null) {
                        stream.close();
                    }

                }
            } else if (fileSize < remoteSize) {
                throw new StorageException("Falha ao enviar documento. O tamanho do arquivo remoto é maior que o arquivo local");
            }
        } catch (HttpException e) {
            throw new StorageException(e.getMessage());
        } catch (IOException e) {
            throw new StorageException(e.getMessage());
        } catch (InterruptedException e) {
            throw new StorageException(e.getMessage());
        } finally {
            if (temp != null) {
                temp.delete();
            }
            hashes.remove(hash);
            return hash;
        }
    }

    private HttpClient getHttpClient() {
        return (new Connection(baseUrl, user, senha, 100)).getHttpClient();
    }

    private InputStream createStreamAfterPos(File file, long pos) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        stream.skip(pos);
        return stream;
    }

    private static File createFile(InputStream stream) throws IOException {
        File file = File.createTempFile(UUID.randomUUID().toString(), "bin");
        OutputStream out = null;

        File var3;
        try {
            out = new FileOutputStream(file);
            IOUtils.copyLarge(stream, out);
            var3 = file;
        } finally {
            if (out != null) {
                out.close();
            }

        }

        return var3;
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
        throw new StorageException("Método Inválido");
    }
}
