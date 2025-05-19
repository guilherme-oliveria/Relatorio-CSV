package br.jus.tjro.gabinete.service.remoto;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.interfaces.importacao.FonteDados;
import br.jus.tjro.gabinete.model.gab.transiente.TipoDocumentoRemoto;
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
public class TipoDocumentoRemotoService {

    private final List<FonteDados> fontes;
    private final RestTemplate restTemplate;

    @Autowired
    public TipoDocumentoRemotoService(List<FonteDados> fontes, RestTemplate restTemplate) {
        this.fontes = fontes;
        this.restTemplate = restTemplate;
    }

    private String getUrlBase(FonteDadosEnum fonte) throws Exception {
        return getFonte(fonte).getUrlBase();
    }

    private FonteDados getFonte(FonteDadosEnum fonte) throws Exception {
        return fonte.selecionaFonte(fontes);
    }

    public List<TipoDocumentoRemoto> buscaTiposDeDocumentosNoWebService(FonteDadosEnum fonte) throws Exception {

        ResponseEntity<TipoDocumentoRemoto[]> response;
        try {
            RequestEntity<Void> request = RequestEntity
                .get(URI.create(getUrlBase(fonte) + "/tipo-documento")).build();
            response = restTemplate.exchange(request, TipoDocumentoRemoto[].class);

        } catch (ResourceAccessException e) {
            throw new ResourceAccessException("Não foi possível estabelecer conexão com a fonte de dados");
        }
        return Arrays.asList(response.getBody());
    }
}
