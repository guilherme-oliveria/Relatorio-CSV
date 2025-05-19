package br.jus.tjro.gabinete.service.remoto.tpu;

import br.jus.tjro.gabinete.model.gab.tpu.TpuClasse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class TpuClasseRemotoService extends TpuRemotoServiceAbstract {

    final String CLASSE_RESOURCE_NAME = "classes";

    private final String url;

    @Autowired
    public TpuClasseRemotoService(@Value("${URL_TPU:https://tpu.tjro.jus.br/api/v1}") String url, RestTemplate restTemplate) {
        super(restTemplate);
        this.url = url;
    }

    @Retryable
    @Cacheable(value = "tpuClasse", unless = "#result == null or #result.getDescricao().contains('esta Indisponivel')")
    public TpuClasse getClasse(Long codigo) {
        return findByCodigo(codigo, classesUrl());
    }

    @Retryable
    @Cacheable(value = "tpuClasseArvore")
    public List<TpuClasse> getArvoreClasses(Long id) {
        return findArvore(id, classesUrl());
    }

    @Recover
    public List<TpuClasse> getDefaultArvoreClasses(Long codigos){
        var arvore = new ArrayList<TpuClasse>();
        arvore.add(getDefaultClasse(codigos, null));
        return arvore;
    }

    @Recover
    public TpuClasse getDefaultClasse(Long codigo, Throwable e){
        if(e != null)
            e.printStackTrace();
        return new TpuClasse(codigo,
            codigo+" Serviço da tpu esta Indisponivel",
            "",null,"",false);
    }

    private String getURL() {
        return url;
    }

    private String classesUrl() {
        return getURL() + "/" + CLASSE_RESOURCE_NAME;
    }

}
