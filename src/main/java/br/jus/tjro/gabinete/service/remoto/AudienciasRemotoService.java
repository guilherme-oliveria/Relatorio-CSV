package br.jus.tjro.gabinete.service.remoto;

import br.jus.tjro.gabinete.model.gab.Audiencia;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Service
public class AudienciasRemotoService {

    private final String host;
    private final RestTemplate restTemplate;

    @Autowired
    public AudienciasRemotoService(@Value("${audiencia.host:https://aud-api.tjro.jus.br/v1}") String host, RestTemplate restTemplate) {
        this.host = host;
        this.restTemplate = restTemplate;
    }

    public List<Audiencia> getAudiencias(Processo processo, Usuario usuario) throws Exception {
        RequestEntity<Void> request;
        try {
            String numProcesso = processo.getNumeroProcesso();
            String cpf = usuario.getCpf();
            request = RequestEntity.get(URI.create(host+"/audiencias-gravadas/"+numProcesso+"/GABINETE/"+cpf+"/200.1.1.1/"))
                .build();
            return Arrays.asList(restTemplate.exchange(request, Audiencia[].class).getBody());
        } catch (ResourceAccessException e) {
            throw new ResourceAccessException("Não foi possível estabelecer conexão o seriço de audiências");
        }
    }
}
