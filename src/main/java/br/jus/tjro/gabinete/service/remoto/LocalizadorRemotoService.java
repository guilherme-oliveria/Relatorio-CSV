package br.jus.tjro.gabinete.service.remoto;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorAgrupador;
import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorCaixa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class LocalizadorRemotoService extends RemotoServiceAbstract {

    private final RestTemplate restTemplate;
    private final String url;

    @Autowired
    public LocalizadorRemotoService(@Value("${localizadores.url:http://localhost:8080}") String url, RestTemplate restTemplate) {
        this.url = url + "/localizadores";
        this.restTemplate = restTemplate;
    }
    
    public List<LocalizadorAgrupador> buscaLocalizadorAgrupador(List<LocalizadorCaixa> localizadores) throws ServicoRemotoException {
        localizadores.stream().forEach(c -> {c.criaRegras();});
        var array = post(url + "/quantidades", localizadores, LocalizadorAgrupador[].class,"Erro ao carregar localizadores").getBody();
        return Arrays.asList(array);
    }


    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public List<Long> buscaProcessoLocalizador(LocalizadorCaixa localizador) throws ServicoRemotoException {
        localizador.criaRegras();
        var array = post(url + "/processos", localizador, Long[].class, null).getBody();
        return Arrays.asList(array);
    }
}
