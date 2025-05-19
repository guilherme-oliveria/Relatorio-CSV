package br.jus.tjro.gabinete.service.remoto;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.interfaces.importacao.FonteDados;
import br.jus.tjro.gabinete.model.gab.localizador.Caixa;
import br.jus.tjro.gabinete.util.ParametrosUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CaixaRemotoService extends RemotoServiceAbstract {

    private final RestTemplate restTemplate;
    private final List<FonteDados> fontes;

    @Autowired
    public CaixaRemotoService(List<FonteDados> fonte,
                              RestTemplate restTemplate) {
        this.fontes = fonte;
        this.restTemplate = restTemplate;
    }

    public String getUrlBase(FonteDadosEnum fonte) throws Exception {
        return fontes.stream()
            .filter(f -> f.getFonteDadosEnum() == fonte)
            .findAny()
            .orElseThrow(() -> new Exception("Nao foi possivel encontrar sistema para o parametro"+fonte))
            .getUrlBase() + "/processo/caixa/";
    }

    public String getCaixaPorIdProcesso(Long idProcesso, FonteDadosEnum sistema) throws Exception {
        RequestEntity<Void> request = get(getUrlBase(sistema) + idProcesso);
        Caixa caixa = restTemplate.exchange(request, Caixa.class).getBody();
        return caixa != null ? caixa.getNome() : "Decisão";
    }

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
