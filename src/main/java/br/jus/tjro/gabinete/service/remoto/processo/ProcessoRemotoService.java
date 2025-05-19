package br.jus.tjro.gabinete.service.remoto.processo;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.api.RequisicaoProcessoSegredo;
import br.jus.tjro.gabinete.api.RequisicaoRetornoConcluso;
import br.jus.tjro.gabinete.api.util.CodigoSegurancaUtils;
import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.interfaces.importacao.FonteDados;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.localizador.Caixa;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.transiente.ProcessoConcluso;
import br.jus.tjro.gabinete.service.local.CaixaService;
import br.jus.tjro.gabinete.service.local.PrioridadeProcessualService;
import br.jus.tjro.gabinete.service.remoto.AssuntoRemotoService;
import br.jus.tjro.gabinete.service.remoto.CaixaRemotoService;
import br.jus.tjro.gabinete.service.remoto.RemotoServiceAbstract;
import io.vavr.collection.Array;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProcessoRemotoService extends RemotoServiceAbstract {

    private final List<FonteDados> fontes;
    private final RestTemplate restTemplate;

    @Autowired
    private AssuntoRemotoService assuntoRemotoService;
    @Autowired
    private CaixaRemotoService caixaRemotoService;
    @Autowired
    private CaixaService caixaService;
    @Autowired
    private PrioridadeProcessualService prioridadeProcessualService;
    @Value("${tribunal}")
    String tribunal;

    private static final Logger logger = LoggerFactory.getLogger(ProcessoRemotoService.class);

    @Autowired
    public ProcessoRemotoService(List<FonteDados> fontes, RestTemplate restTemplate) {
        this.fontes = fontes;
        this.restTemplate = restTemplate;
    }

    private String getUrlBase(FonteDadosEnum fonte) {
        return getFonte(fonte).getUrlBase() + "/processo";
    }

    private FonteDados getFonte(FonteDadosEnum fonte) {
        return fonte.selecionaFonte(fontes);
    }

    public ProcessoConcluso[] buscaProcessosConclusos(FonteDadosEnum fonteDados) throws Exception {
        RequestEntity<Void> request = get(getUrlBase(fonteDados) + "/conclusos");
        return restTemplate.exchange(request, ProcessoConcluso[].class).getBody();
    }

    public ProcessoConcluso buscaProcessoConcluso(FonteDadosEnum fonteDados, Long idProcesso) throws Exception {
        RequestEntity<Void> request = get(getUrlBase(fonteDados) + "/conclusos/"+idProcesso);
        ProcessoConcluso processoConcluso = restTemplate.exchange(request, ProcessoConcluso.class).getBody();
        return processoConcluso;

    }

    public boolean confirmaRecebimentoProcesso(Long idProcesso, FonteDadosEnum fonte) throws Exception {
        String url = getUrlBase(fonte) + "/integrado";
        Boolean resul = false;
        logger.info("Confirmando recebimento do processo. Id legado = "+ idProcesso);
        RequestEntity<Long> request = RequestEntity.post(URI.create(url))
            .contentType(MediaType.APPLICATION_JSON_UTF8).body(idProcesso);
        ResponseEntity<Boolean> response = restTemplate.exchange(request, Boolean.class);
        resul = response.getBody() != null;
        if(!resul) {
            logger.error("Erro ao confirmar o recebimento do processo. Id legado = " + idProcesso);
            return resul;
        }
        logger.info("Concluindo o recebimento do processo. Id legado = "+ idProcesso);
        return resul;
    }

    public String devolveProcessoOrigem(RequisicaoRetornoConcluso requisicao, FonteDadosEnum fonte) throws Exception {
        String idLegado = null;
        try {
            ResponseEntity<String> response =  post(getUrlBase(fonte) + "/retorno-concluso",requisicao,String.class,null);
            if (response.getStatusCode() == HttpStatus.OK)
                idLegado = response.getBody();

            if(idLegado == null || idLegado.isEmpty()) {
                String mess = "Não foi possível obter o id legado para o processo: " + requisicao.getIdProcesso();
                throw new Exception(mess);
            }
            return idLegado;
        } catch (HttpServerErrorException eHttp) {
            List<String> list = eHttp.getResponseHeaders().get("error");
            if (list != null && list.size() > 0){
                throw new Exception(list.get(0),eHttp);
            }
            String mess = "Erro generico ao enviar a origem. ID_PROCESSO: " + requisicao.getIdProcesso();
            throw new Exception(mess,eHttp);
        }catch (ResourceAccessException ra){
            throw new Exception("Erro na comunicação com o servidor de origem.",ra);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public String atualizarSegredoJustica(Processo processo, FonteDadosEnum fonteDados, Usuario usuario) throws Exception {
        try {
            RequisicaoProcessoSegredo payLoad;
            if(tribunal.equals("tjmg")) {
                payLoad = new RequisicaoProcessoSegredo(processo.getIdProcessoSistemaLegado().toString(), processo.isSegredoJustica(), processo.getMotivoSegredoJustica(), usuario.getCpf());
            } else {
                String codigoSeguranca = CodigoSegurancaUtils.gerarCodigoSeguranca("ambienteTeste");
                payLoad = new RequisicaoProcessoSegredo(processo.getIdProcessoSistemaLegado().toString(), processo.isSegredoJustica(), processo.getMotivoSegredoJustica(), codigoSeguranca);
            }
            ResponseEntity<String> retorno = post(getUrlBase(fonteDados) + "/atualizar-sigilo", payLoad, String.class, null);
            return retorno.getBody();
        } catch (Exception e) {
            var msg = "Erro ao atualizar sigilo do proceeo no sistema legado: " + processo.getId();
            logger.error(msg,e);
            throw new Exception(msg,e);
        }
    }

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public List<String> buscaTarefaAtual(Processo processo) throws ServicoRemotoException {
        RequestEntity<Void> request = get(getUrlBase(processo.getFonteDados()) + "/tarefa/"+processo.getIdProcessoSistemaLegado());
        try {
            String[] body = restTemplate.exchange(request, String[].class).getBody();
            if(body == null)
                return List.of();
            return Arrays.asList(body);
        }catch (Exception e) {
            throw new ServicoRemotoException("Erro ao buscar a tarefa do processo " + processo.getNumeroProcesso(), e);
        }
    }

    public Optional<ProcessoConcluso> findByNumero(String numeroProcesso, FonteDados fonte){
        try {
            RequestEntity<Void> request = get(getUrlBase(fonte.getFonteDadosEnum()) + "/numero/"+numeroProcesso.replaceAll("[^0-9]",""));
            ProcessoConcluso processo = restTemplate.exchange(request, ProcessoConcluso.class).getBody();
            if(processo != null)
                processo.setFonteDadosEnum(fonte.getFonteDadosEnum());
            return Optional.of(processo);
        } catch (Exception e) {
            var msg = "Erro ao buscar processo no sistema legado {"+fonte.getFonteDadosEnum()+"} pelo numero "+numeroProcesso;
            logger.error(msg,e);
            return Optional.empty();
        }
    }

    public String[] processosRelacionados(Long idProcessoLegado, FonteDadosEnum fonte) {
        try {
            RequestEntity<Void> request = get(getUrlBase(fonte) + "/relacionados/" + idProcessoLegado);
            return restTemplate.exchange(request, String[].class).getBody();
        } catch (Exception e) {
            var msg = "Erro ao buscar os processos relacionados ao processo {"+fonte+"} pelo numero "+idProcessoLegado;
            logger.error(msg,e);
            return new String[]{""};
        }
    }
}
