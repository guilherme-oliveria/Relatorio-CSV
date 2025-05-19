package br.jus.tjro.gabinete.service.remoto.tpu;

import br.jus.tjro.gabinete.model.gab.tpu.TpuAssunto;
import br.jus.tjro.gabinete.util.ParametrosUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class TpuAssuntoRemotoService extends TpuRemotoServiceAbstract {

    final String ASSUNTO_RESOURCE_NAME = "assuntos";
    private final String url;

    @Autowired
    private ParametrosUtil parametro;

    @Autowired
    public TpuAssuntoRemotoService(@Value("${URL_TPU:https://tpu.tjro.jus.br/api/v1}") String url, RestTemplate restTemplate) {
        super(restTemplate);
        this.url = url;
    }


    @Retryable
    public TpuAssunto getAssunto(Long codigo) throws Exception {
        return findByCodigo(codigo, assuntoUrl());
    }

    @Retryable
    public List<TpuAssunto> getAssuntos(List<Long> codigos) throws Exception {
        return findByCodigos(codigos, assuntoUrl());
    }

    @Retryable
    public List<TpuAssunto> getArvoreAssuntos(Long id) throws Exception {
        return findArvore(id, assuntoUrl());
    }

    @Recover
    public TpuAssunto getDefaultAssunto(Long codigo){
        return new TpuAssunto(codigo,codigo+" Serviço da TPU esta Indisponivel","",null,"",false);
    }

    @Recover
    public List<TpuAssunto> getDefaultAssuntos(List<Long> codigos){
        var assuntos = new ArrayList<TpuAssunto>();
        codigos.forEach(id -> assuntos.add(getDefaultAssunto(id)));
        return assuntos;
    }

    @Recover
    public List<TpuAssunto> getDefaultArvoreAssuntos(Long codigos){
        var arvore = new ArrayList<TpuAssunto>();
        arvore.add(getDefaultAssunto(codigos));
        return arvore;
    }

    private String getURL() {
        return url;
    }

    private String assuntoUrl() {
        return getURL() + "/" + ASSUNTO_RESOURCE_NAME;
    }
}
