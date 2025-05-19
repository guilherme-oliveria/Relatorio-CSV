package br.jus.tjro.gabinete.service.remoto;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.interfaces.importacao.FonteDados;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Service
public class DocumentoRemotoService extends RemotoServiceAbstract {

    private final List<FonteDados> fontes;
    private final RestTemplate restTemplate;

    @Autowired
    public DocumentoRemotoService(List<FonteDados> fontes, RestTemplate restTemplate) {
        this.fontes = fontes;
        this.restTemplate = restTemplate;
    }

    private final Logger looger = LoggerFactory.getLogger(DocumentoRemotoService.class);

    private String getUrlBase(FonteDadosEnum fonte) throws Exception {
        return getFonte(fonte).getUrlBase() + "/documento";
    }

    private FonteDados getFonte(FonteDadosEnum fonte) throws Exception {
        return fonte.selecionaFonte(fontes);
    }

    public Minuta getDocumento(String idDocumentoLegado, FonteDadosEnum fonte, Usuario usuario) throws Exception {
        ResponseEntity<Minuta> response;
        RequestEntity<Void> request = get(getUrlBase(fonte) + "/" + idDocumentoLegado + "/");
        response = restTemplate.exchange(request, Minuta.class);
        return response.getBody();
    }

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public String atualizaDataJuntada(Date dataJuntada, String idDocumento, FonteDadosEnum fonte, Usuario usuario) throws Exception {
        ResponseEntity<String> request = post(getUrlBase(fonte) + "/atualiza/data-juntada/" + idDocumento, dataJuntada, String.class);
        return request.getBody();

    }
}
