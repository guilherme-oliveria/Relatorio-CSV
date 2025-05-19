package br.jus.tjro.gabinete.service.remoto;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.interfaces.importacao.FonteDados;
import br.jus.tjro.gabinete.model.gab.transiente.PessoaDocumentosTransiente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
public class PessoaDocumentosRemotoService extends RemotoServiceAbstract {

    private final List<FonteDados> fontes;
    private final RestTemplate restTemplate;

    @Autowired
    public PessoaDocumentosRemotoService(List<FonteDados> fontes, RestTemplate restTemplate) {
        this.fontes = fontes;
        this.restTemplate = restTemplate;
    }

    private String getUrlBase(FonteDadosEnum fonte) throws Exception {
        return getFonte(fonte).getUrlBase() + "/pessoa_doc/";
    }

    private FonteDados getFonte(FonteDadosEnum fonte) throws Exception {
        return fonte.selecionaFonte(fontes);
    }

    public PessoaDocumentosTransiente[] buscaDocumentosDaPessoa(
        Long idPessoa, FonteDadosEnum fonte) throws Exception {

        RequestEntity<Void> request;
        request = get(getUrlBase(fonte) + idPessoa);

        return restTemplate.exchange(request, PessoaDocumentosTransiente[].class).getBody();
    }

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
