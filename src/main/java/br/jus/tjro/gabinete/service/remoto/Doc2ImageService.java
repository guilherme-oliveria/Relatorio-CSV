package br.jus.tjro.gabinete.service.remoto;

import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.util.InputStreamUtil;
import br.jus.tjro.gabinete.util.ParametrosUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Service
public class Doc2ImageService extends RemotoServiceAbstract{

    private final RestTemplate restTemplate;
    private final String urlBase;

    @Autowired
    public Doc2ImageService(@Value("${URL_DOC_2_IMG:http://localhost:3000}") String urlBase, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.urlBase = urlBase;
    }

    public String getUrlBase() {
        return urlBase;
    }

    public byte[] binario2Image(byte[] bytesArquivo, StringBuilder extensao) throws IOException, ServicoRemotoException {
        ResponseEntity<byte[]> response = doc2Image(bytesArquivo, getUrlBase());
        if (extensao != null) {
            extensao.append(response.getHeaders().getFirst("Content-Type"));
        }
        return response.getBody();
    }

    public byte[] html2Image(String html) throws IOException, ServicoRemotoException {
        return doc2Image(html.getBytes(StandardCharsets.UTF_8), getUrlBase()).getBody();
    }

    private ResponseEntity<byte[]> doc2Image(byte[] bytes, String url) throws IOException, ServicoRemotoException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("arquivo", bytes);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(
            map, headers);
        return post(url, request, byte[].class,"O serviço de criação das imagens está temporariamente indisponível",
            MediaType.MULTIPART_FORM_DATA);
    }

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
