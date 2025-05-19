package br.jus.tjro.gabinete.service.remoto.tpu;

import br.jus.tjro.gabinete.model.gab.tpu.TpuComplemento;
import br.jus.tjro.gabinete.model.gab.tpu.TpuMovimento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static io.vavr.collection.Stream.ofAll;

@Service
public class TpuMovimentoRemotoService extends TpuRemotoServiceAbstract {

    final String MOVIMENTO_RESOURCE_NAME = "movimentos";


    private TpuComplemento tpuComplemento;
    private final String url;

    @Autowired
    public TpuMovimentoRemotoService(@Value("${URL_TPU:https://tpu.tjro.jus.br/api/v1}") String url, RestTemplate restTemplate) {
        super(restTemplate);
        this.url = url;
    }

    @Retryable
    public List<TpuMovimento> getMovimentos(List<Long> codigos) throws Exception {
        return findByCodigos(codigos, movimentoUrl());
    }

    @Retryable
    public TpuMovimento getMovimento(Long codigo) {
        return findByCodigo(codigo, movimentoUrl());
    }

    @Retryable
    public TpuMovimento getMovimentoComComplementos(Long codigo) {
        TpuMovimento movimento = findByCodigo(codigo, movimentoUrl());
        List<TpuComplemento> complementos = getComplementosDoMovimento(codigo);
        movimento.setComplementos(complementos);
        return movimento;
    }

    @Retryable
    public List<TpuMovimento> getArvoreMovimentos(Long id) throws Exception {
        return findArvore(id, movimentoUrl());
    }

    @Retryable
    public List<TpuComplemento> getComplementosDoMovimento(Long codigoMovimento) {
        return getComplementos(codigoMovimento, getURL());
    }

    public TpuComplemento getComplemento(Long codigoComplemento) {
        return tpuComplemento.findByCodigo(codigoComplemento);
    }

    @Retryable
    public List<TpuMovimento> getMovimentosComComplemento(List<Long> codigos) throws Exception {
        List<TpuMovimento> movimentos = getMovimentos(codigos);
        movimentos.forEach(mv -> {
            List<TpuComplemento> complementos = getComplementosDoMovimento(mv.getCodigo());
            mv.setComplementos(complementos);
        });
        return movimentos;
    }

    private String getURL() {
        return url;
    }

    private String movimentoUrl() {
        return getURL() + "/" + MOVIMENTO_RESOURCE_NAME;
    }

    @Recover
    public List<TpuMovimento> getDefaultMovimentos(Throwable t, List<Long> codigos){
        return ofAll(codigos).map(c -> {
            final DefaultMovimento movimento = new DefaultMovimento(c);
            movimento.addFilho(new DefaultMovimento(12164l, "Outras Decisões"));
            return (TpuMovimento) movimento;
        }).asJava();
    }

    class DefaultMovimento extends TpuMovimento {
        DefaultMovimento(Long codigo,String descricao) {
            super(codigo, descricao, "glossario", null, "true", false);
        }
        DefaultMovimento(Long codigo) {
            this(codigo, "Serviço de movimento Indisponivel");
        }
    }

    @Recover
    public List<TpuMovimento> getDefaultArvoreMovimentos(Throwable t,Long codigo){
        ArrayList<Long> cods = new ArrayList<>();
        cods.add(codigo);
        return getDefaultMovimentos(t,cods);
    }

    @Recover
    public List<TpuComplemento> getDefaultComplemento(Throwable t, Long codigoMovimento){
        return new ArrayList<>();
    }

    @Recover
    public TpuMovimento getDefaultMovimento(Throwable t, Long codigo){
        return new DefaultMovimento(codigo,"Serviço de movimento Indisponivel");
    }
}
