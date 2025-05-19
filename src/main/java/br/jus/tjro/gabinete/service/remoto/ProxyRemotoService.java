package br.jus.tjro.gabinete.service.remoto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class ProxyRemotoService extends RemotoServiceAbstract {

    private final RestTemplate restTemplate;

    @Autowired
    public ProxyRemotoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Object getBody(String url) throws Exception {
        return this.restTemplate.getForObject(url, Object.class);
    }


    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
