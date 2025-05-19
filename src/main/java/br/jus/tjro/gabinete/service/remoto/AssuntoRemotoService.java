package br.jus.tjro.gabinete.service.remoto;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.interfaces.importacao.FonteDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Service
public class AssuntoRemotoService {

    private final List<FonteDados> fontes;
    private final RestTemplate restTemplate;

    @Autowired
    public AssuntoRemotoService(List<FonteDados> fontes, RestTemplate restTemplate) {
        this.fontes = fontes;
        this.restTemplate = restTemplate;
    }

    private String getUrlBase(FonteDadosEnum fonte) throws Exception {
        return getFonte(fonte).getUrlBase();
    }

    private FonteDados getFonte(FonteDadosEnum fonte) throws Exception {
        return fonte.selecionaFonte(fontes);
    }

    public String buscaAssuntoPrincipalDoProcessoNoWebService(Long idProcesso, FonteDadosEnum fonte) throws Exception {

        RequestEntity<Void> request;
        try {
            request = RequestEntity.get(URI.create(getUrlBase(fonte) + "/assunto/principal/descricao/processo/" + idProcesso))
                .build();
        } catch (ResourceAccessException e) {
            throw new ResourceAccessException("Não foi possível estabelecer conexão com a fonte de dados");
        }
        return restTemplate.exchange(request, String.class).getBody();
    }

    public List<Long> getIdsAssuntosProcesso(long idProcessoLegado, FonteDadosEnum fonte) throws Exception {
        ResponseEntity<Long[]> response;
        try {
            RequestEntity<Void> request = RequestEntity
                .get(URI.create(getUrlBase(fonte) + "/assunto/ids/processo/" + idProcessoLegado)).build();
            response = restTemplate.exchange(request, Long[].class);
        } catch (ResourceAccessException e) {
            throw new ResourceAccessException("Não foi possível estabelecer conexão com o web service do PJE");
        }
        return Arrays.asList(response.getBody());
    }

    public String buscaIdCnjDoAssuntoPrincipalDoProcessoNoWebService(Long idProcesso, FonteDadosEnum fonte)
        throws Exception {

        RequestEntity<Void> request;
        try {
            request = RequestEntity.get(URI.create(getUrlBase(fonte) + "/assunto/id/principal/processo/" + idProcesso))
                .build();
        } catch (ResourceAccessException e) {
            throw new ResourceAccessException("Não foi possível estabelecer conexão com a fonte de dados");
        }
        return restTemplate.exchange(request, String.class).getBody();
    }
}
