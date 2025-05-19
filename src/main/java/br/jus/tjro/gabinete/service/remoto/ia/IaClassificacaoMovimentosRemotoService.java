package br.jus.tjro.gabinete.service.remoto.ia;

import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.model.gab.ia.RespostaGeradorTexto;
import br.jus.tjro.gabinete.model.gab.ia.SolicitacaoGeradorTexto;
import br.jus.tjro.gabinete.model.gab.ia.SolicitacaoSugestaoTipoMovimento;
import br.jus.tjro.gabinete.service.local.tpu.TpuMovimentoService;
import br.jus.tjro.gabinete.service.remoto.RemotoServiceAbstract;
import br.jus.tjro.sinapses.api.modelo.ClassificacaoClasse;
import br.jus.tjro.sinapses.api.modelo.ClassificacaoClasseConviccao;
import br.jus.tjro.sinapses.api.modelo.ClassificacaoResultado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class IaClassificacaoMovimentosRemotoService extends RemotoServiceAbstract{

    @Autowired
    TpuMovimentoService tpuMovimentoService;

    private final RestTemplate restTemplate;

    private final Logger looger = LoggerFactory.getLogger(IaClassificacaoMovimentosRemotoService.class);

    @Value("${IA_TIPO_CLASSIFICADOR:CLS_TIPO_MOVIMENTO_MAGISTRADO}")
    private String nomeClassificador;

    @Value("${IA_ATIVADO:false}")
    private Boolean iaAtivado;

    @Value("${IA_HOST:localhost:8666}")
    private String urlServico;

    @Value("${IA_USER:gabiente}")
    private String usuario;

    @Value("${IA_PASSWORD:senha}")
    private String senha;

    @Value("${IA_QTD_RAMIFICACOES:3}")
    private Integer qtdeRamificacoes;

    @Value("${IA_QTD_PALAVRAS:3}")
    private Integer qtdePalavras;

    @Autowired
    public IaClassificacaoMovimentosRemotoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retryable
    public ClassificacaoResultado buscaRecomendacaoDeMovimento(String textoBase64) throws ServicoRemotoException {
        if(iaAtivado) {
            SolicitacaoSugestaoTipoMovimento solicitacaoSugestaoTipoMovimento = new SolicitacaoSugestaoTipoMovimento(textoBase64);
            String url = urlServico + "/modelo/executarServico/-tjro-jud/" + nomeClassificador + "/2";
            String credenciais = Base64.getEncoder().encodeToString((usuario + ":" + senha).getBytes());
            RequestEntity<SolicitacaoSugestaoTipoMovimento> entity = RequestEntity.post(URI.create(url))
                .header("Authorization", "Basic " + credenciais)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(solicitacaoSugestaoTipoMovimento);
            ClassificacaoResultado classificacaoResultado = restTemplate.exchange(entity, ClassificacaoResultado.class).getBody();
            IaFiltroMovimentosInvalidos iaFiltroMovimentosInvalidos = new IaFiltroMovimentosInvalidos(tpuMovimentoService);
            iaFiltroMovimentosInvalidos.filtrar(classificacaoResultado);
            return classificacaoResultado;
        } else {
            return null;
        }
    }

    @Recover
    public ClassificacaoResultado getDefaultRecomendacoes(String textoBase64, Throwable t) {
        looger.error("Erro ao buscar Recomendações de movimento para a minuta",t);
        var resultado = new ClassificacaoResultado();
        var classe = new ClassificacaoClasse();
        classe.setCodigo("");
        classe.setDescricao("");
        resultado.setClasseConvicto(classe);
        resultado.setResultados(List.of(new ClassificacaoClasseConviccao(classe,new BigDecimal(1))));
        return resultado;
    }

    @Retryable
    public List<String> geradorTexto(String texto) throws ServicoRemotoException {
        if(iaAtivado) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            SolicitacaoGeradorTexto solicitacao = new SolicitacaoGeradorTexto(texto, qtdeRamificacoes, qtdePalavras);
            HttpEntity<SolicitacaoGeradorTexto> request = new HttpEntity<>(solicitacao, headers);
            String uri = urlServico + "/modelo/executarServico/-/GEN_GERADOR_TEXTO_MAGISTRADO/1";
            return this
                .post(uri, request, RespostaGeradorTexto.class,
                    "Não foi possível estabelecer conexão com o serviço de geração inteligente de textos")
                .getBody().getTextos();
        } else {
            return new ArrayList<>();
        }
    }

    @Recover
    public List<String> getDefaultGeradorTexto(Throwable t,String texto) {
        looger.error("Erro ao pegar gerador de texto",t);
        return new ArrayList<>();
    }

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
