package br.jus.tjro.gabinete.service.remoto;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.interfaces.importacao.FonteDados;
import br.jus.tjro.gabinete.model.gab.ResponsePage;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoDocumento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Service
public class ProcessoDocumentoRemotoService extends RemotoServiceAbstract {

    private final List<FonteDados> fontes;
    private final RestTemplate restTemplate;

    @Autowired
    public ProcessoDocumentoRemotoService(List<FonteDados> fontes, RestTemplate restTemplate) {
        this.fontes = fontes;
        this.restTemplate = restTemplate;
    }

    private String getUrlBase(FonteDadosEnum fonte) throws Exception {
        return getFonte(fonte).getUrlBase() + "/documento";
    }

    private FonteDados getFonte(FonteDadosEnum fonte) throws Exception {
        return fonte.selecionaFonte(fontes);
    }

    public ProcessoDocumento getDocumento(int idDocumentoLegado, FonteDadosEnum fonte) throws Exception {
        try{
            ResponseEntity<ProcessoDocumento> response;
            RequestEntity<Void> request = get(getUrlBase(fonte) + "/" + idDocumentoLegado + "/");
            response = restTemplate.exchange(request, ProcessoDocumento.class);
            return response.getBody();
        }catch (Throwable t){
            throw new Exception("Erro ao carregar documento da fonte de dados ["+fonte.fonte+"]",t);
        }
    }

    public ResponsePage<ProcessoDocumento> getDocumentosProcesso(Processo processo, int pagina) throws Exception {
        Long idProcessoLegado = processo.getIdProcessoSistemaLegado();
        FonteDadosEnum fonte = processo.getSistema();
        ParameterizedTypeReference<ResponsePage<ProcessoDocumento>> responseType = new ParameterizedTypeReference<ResponsePage<ProcessoDocumento>>() { };
        URI url = URI.create(getUrlBase(fonte) + "/completos/processo/" + idProcessoLegado + "/pagina/"+pagina);
        return restTemplate.exchange(url,HttpMethod.GET,null, responseType).getBody();
    }

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
