package br.jus.tjro.gabinete.service.remoto.alvara;

import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.alvara.ContaJudicial;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.service.remoto.RemotoServiceAbstract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ContasJudiciaisRemotoService extends RemotoServiceAbstract {


    private final RestTemplate restTemplate;
    private final String url;

    public ContasJudiciaisRemotoService(RestTemplate restTemplate,
                                        @Value("${integracao.caixa.url:https://integracaocaixa.tjro.jus.br}") String url){
//        @Value("${integracao.caixa.url:http://localhost:8080}") String url){
        this.restTemplate = restTemplate;
        this.url = url+"/conta/consultaSISDEJUD";

    }

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public List<ContaJudicial> findByProcesso(String numeroProcesso, Usuario usuario) throws ServicoRemotoException {
        return Arrays.asList(post(url,numeroProcesso,ContaJudicial[].class, null).getBody());
    }
}
