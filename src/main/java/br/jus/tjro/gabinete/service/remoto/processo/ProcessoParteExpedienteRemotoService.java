package br.jus.tjro.gabinete.service.remoto.processo;

import br.jus.tjro.gabinete.interfaces.importacao.FonteDados;
import br.jus.tjro.gabinete.interfaces.importacao.RemotoRepository;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoExpediente;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParteExpediente;
import br.jus.tjro.gabinete.model.gab.transiente.ProcessoParteExpedienteTransiente;
import br.jus.tjro.gabinete.service.remoto.RemotoServiceAbstract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class ProcessoParteExpedienteRemotoService extends RemotoServiceAbstract implements RemotoRepository<ProcessoParteExpedienteTransiente> {

    private final RestTemplate restTemplate;

    @Autowired
    public ProcessoParteExpedienteRemotoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ProcessoParteExpedienteTransiente findOne(String idLegado, FonteDados fonteDados) throws Exception {
        String url = fonteDados.getUrlBase();
        RequestEntity<Void> request = get(url + "/expediente/" + idLegado);
        ResponseEntity<ProcessoParteExpedienteTransiente> response;
        response = restTemplate.exchange(request, ProcessoParteExpedienteTransiente.class);
        return response.getBody();
    }

    @Override
    public ProcessoParteExpedienteTransiente[] findAllForProcesso(Processo processo, FonteDados fonteDados) throws Exception {
        String url = fonteDados.getUrlBase();
        RequestEntity<Void> request = get(url + "/expediente/processo/" + processo.getIdProcessoSistemaLegado());
        ResponseEntity<ProcessoParteExpedienteTransiente[]> response;
        response = restTemplate.exchange(request, ProcessoParteExpedienteTransiente[].class);
        return response.getBody();
    }

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
