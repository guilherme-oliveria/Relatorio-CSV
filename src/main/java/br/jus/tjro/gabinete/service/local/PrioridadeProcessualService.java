package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.model.gab.processo.PrioridadeProcessual;
import br.jus.tjro.gabinete.repository.gab.processo.PrioridadesProcessuaisRepository;
import br.jus.tjro.gabinete.util.ParametrosUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Service
public class PrioridadeProcessualService {

    private final ParametrosUtil parametro;
    private final RestTemplate restTemplate;
    private final PrioridadesProcessuaisRepository prioridadesProcessuaisRepository;

    @Autowired
    public PrioridadeProcessualService(ParametrosUtil parametro, RestTemplate restTemplate, PrioridadesProcessuaisRepository prioridadesProcessuaisRepository) {
        this.parametro = parametro;
        this.restTemplate = restTemplate;
        this.prioridadesProcessuaisRepository = prioridadesProcessuaisRepository;
    }

    public String getUrlBase() {
        return parametro.getValorString("URL_PJE_PG_REST");
    }

    public Integer[] buscaPrioridadesProcessuaisDoProcessoNoWebService(Long idProcesso) {
        RequestEntity<Void> request = RequestEntity
            .get(URI.create(getUrlBase() + "/prioridade-processual/id/processo/" + idProcesso)).build();
        return restTemplate.exchange(request, Integer[].class).getBody();
    }


    public List<PrioridadeProcessual> getPrioridadesProcessuais() {
        return prioridadesProcessuaisRepository.findAll();
    }
}
