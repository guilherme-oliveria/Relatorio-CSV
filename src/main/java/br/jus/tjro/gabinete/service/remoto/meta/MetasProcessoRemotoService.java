package br.jus.tjro.gabinete.service.remoto.meta;


import br.jus.tjro.gabinete.service.remoto.RemotoServiceAbstract;
import br.jus.tjro.gabinete.util.ParametrosUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetasProcessoRemotoService extends RemotoServiceAbstract {

    private final RestTemplate restTemplate;
    private final String url;

    public String getUrlBase() {
        return url;
    }

    @Autowired
    public MetasProcessoRemotoService(@Value("${URL_MICROSERVICE_METAS:http://localhost:4266}") String url, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    public List<String> obterMetasParaNumeroProcesso(String numeroProcesso) throws Exception {
        RequestEntity<Void> request = get(getUrlBase() + "/dados/" + numeroProcesso);
        var retorno = restTemplate.exchange(request, Object[].class).getBody();
        return Arrays.asList(retorno).stream().map(o -> o.toString()).collect(Collectors.toList());
    }

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
