package br.jus.tjro.gabinete.service.remoto.sg;

import br.jus.tjro.gabinete.api.modelo.DocumentoApi;
import br.jus.tjro.gabinete.api.util.CodigoSegurancaUtils;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.interfaces.importacao.FonteDados;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.PedidoInclusaoPauta;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.scheduled.devolve.origem.builder.DocumentoBuilderApi;
import br.jus.tjro.gabinete.scheduled.devolve.origem.builder.VariaveisInstanciaBuilder;
import br.jus.tjro.gabinete.service.assinaturadigital.RetornoAssinaturaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class PedidoInclusaoPautaRemotoService {

    private final String codigoSeguranca;
    private final RetornoAssinaturaService retornoAssinaturaService;
    private final ObjectMapper objectMapper;
    private final DocumentoBuilderApi documentoBuilder;
    private final List<FonteDados> fontes;
    private final RestTemplate restTemplate;

    @Autowired
    public PedidoInclusaoPautaRemotoService(RetornoAssinaturaService retornoAssinaturaService,
                                            List<FonteDados> fontes, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.retornoAssinaturaService = retornoAssinaturaService;
        this.objectMapper = objectMapper;
        this.codigoSeguranca = CodigoSegurancaUtils.gerarCodigoSeguranca("ambienteTeste");
        documentoBuilder = new DocumentoBuilderApi(retornoAssinaturaService);
        this.fontes = fontes;
        this.restTemplate = restTemplate;
    }

    public Minuta enviaPauta(Processo processo) throws Exception {
        if(processo.getFonteDados().equals(FonteDadosEnum.PJEPG))
            throw new Exception("Não é permitido enviar pauta para PJEPG. "+processo.getNumeroProcesso()); 
        if(processo.minutaEmElaboracao().isEmpty())
            throw new Exception("Não existe minuta em elaboração. "+processo.getNumeroProcesso());

        Minuta minuta = processo.minutaEmElaboracao().get();

        if(minuta.getPedidoInclusaoPauta().isEmpty())
            throw new Exception("Não existe pauta em elaboração. "+processo.getNumeroProcesso());

        List<DocumentoApi> acordao = getAcordao(minuta);
        acordao.add(getPauta(minuta.getPedidoInclusaoPauta().get()));
        String idProcessoLegado = processo.getIdProcessoSistemaLegado().toString();
        RequisicaoPautaColegiadoRetornoConcluso requisicao = new RequisicaoPautaColegiadoRetornoConcluso(acordao,
            idProcessoLegado,codigoSeguranca,
            VariaveisInstanciaBuilder.criaVariaveisDaRequisicao(minuta));
        PautaColegiadoRetorno retorno = postPauta(requisicao, processo.getFonteDados());
        minuta.getAnexosExcetoPreliminares().forEach(a -> {
            final String legado = retorno.getLegado(a.getId());
            a.setIdMinutaSistemaLegado(legado);
        });
        System.out.println("Salvar id retorno da pauta e acordao");
        return minuta;
    }


    public PautaColegiadoRetorno postPauta(RequisicaoPautaColegiadoRetornoConcluso requisicao, FonteDadosEnum fonte) throws Exception {
        RequestEntity<RequisicaoPautaColegiadoRetornoConcluso> request;
        PautaColegiadoRetorno pautaRetorno = null;
        try {
            request = RequestEntity.post(URI.create(getUrlBase(fonte)))
                .contentType(APPLICATION_JSON).body(requisicao);
            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            if (response.getStatusCode() == HttpStatus.OK)
                pautaRetorno = objectMapper.readValue(response.getBody(), PautaColegiadoRetorno.class);
            if(pautaRetorno == null) {
                String mess = "Não foi possível obter o id legado para o processo: " + requisicao.getIdProcesso();
                throw new Exception(mess);
            }
        } catch (HttpServerErrorException eHttp) {
            String mess = "Erro generico ao enviar a origem. ID_PROCESSO: " + requisicao.getIdProcesso() + ("O servidor de destino diz: " + eHttp.getResponseBodyAsString());
            throw new Exception(mess,eHttp);
        }catch (ResourceAccessException ra){
            throw new Exception("Erro na comunicação com o servidor de origem.",ra);
        }
        catch (Exception ex) {
            throw ex;
        }
        return pautaRetorno;
    }

    private List<DocumentoApi> getAcordao(Minuta minuta) throws Exception {
        List<DocumentoApi> acordao = minuta.getAnexosExcetoPreliminares().stream()
            .map(it -> {
            try {
                return documentoBuilder.parse(it);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao converter acordao", e);
            }
        }).collect(Collectors.toList());
        minuta.setArquivoAssinado(minuta.getMinutaHtmlRenderizadoBytes());
        acordao.add(documentoBuilder.parse(minuta));
        return acordao;
    }

    private DocumentoApi getPauta(PedidoInclusaoPauta pauta) throws Exception {
        return documentoBuilder.parse(pauta);
    }

    private String getUrlBase(FonteDadosEnum fonte) {
        return getFonte(fonte).getUrlBase() + "/pedido-pauta";
    }

    private FonteDados getFonte(FonteDadosEnum fonte) {
        return fonte.selecionaFonte(fontes);
    }
}
