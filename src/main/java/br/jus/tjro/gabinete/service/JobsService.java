package br.jus.tjro.gabinete.service;

import br.jus.tjro.gabinete.model.gab.processo.PrioridadeProcessual;
import br.jus.tjro.gabinete.repository.gab.processo.PrioridadesProcessuaisRepository;
import br.jus.tjro.gabinete.util.ParametrosUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
@Deprecated
public class JobsService {

    private final ParametrosUtil parametro;

    private final PrioridadesProcessuaisRepository prioridadesProcessuaisRepository;

    private final RestTemplate restTemplate;

    @Autowired
    public JobsService(ParametrosUtil parametro, PrioridadesProcessuaisRepository prioridadesProcessuaisRepository, RestTemplate restTemplate) {
        this.parametro = parametro;
        this.prioridadesProcessuaisRepository = prioridadesProcessuaisRepository;
        this.restTemplate = restTemplate;
    }

    @Deprecated
    public String getUrlBase() {
        return parametro.getValorString("URL_PJE_PG_REST");
    }

    @Deprecated
    public void buscaPrioridadesProcessuais() {
        RequestEntity<Void> request = RequestEntity.get(URI.create(getUrlBase() + "/prioridade-processual")).build();
        PrioridadeProcessual[] prioridadesProcessuais = restTemplate.exchange(request, PrioridadeProcessual[].class)
            .getBody();
        for (PrioridadeProcessual prioridadeProcessual : prioridadesProcessuais) {
            prioridadesProcessuaisRepository.save(prioridadeProcessual);
        }
    }
}
