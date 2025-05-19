package br.jus.tjro.gabinete.service.remoto;

import br.jus.tjro.gabinete.builders.service.BuilderRetornoAssinaturaService;
import br.jus.tjro.gabinete.config.ColegiadoEnvironment;
import br.jus.tjro.gabinete.fonte.dados.PjeSG;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import br.jus.tjro.gabinete.model.gab.minuta.PedidoInclusaoPauta;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.service.assinaturadigital.RetornoAssinaturaService;
import br.jus.tjro.gabinete.service.remoto.sg.PautaColegiadoRetorno;
import br.jus.tjro.gabinete.service.remoto.sg.PedidoInclusaoPautaRemotoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PedidoInclusaoPautaRemotoServiceTests {

    public RetornoAssinaturaService retornoAssinaturaService = BuilderRetornoAssinaturaService.get();
    RestTemplate restTemplate = mock(RestTemplate.class);
    public PedidoInclusaoPautaRemotoService service = new PedidoInclusaoPautaRemotoService(
        retornoAssinaturaService,
        List.of(new PjeSG("windson")), restTemplate, new ObjectMapper());

    public PedidoInclusaoPautaRemotoServiceTests() throws Exception {
        when(retornoAssinaturaService.converterParaAtachado(any(PedidoInclusaoPauta.class))).thenAnswer(invocation -> {
            return "windson".getBytes();
        });

        when(retornoAssinaturaService.converterParaAtachado(any(MinutaAnexo.class))).thenCallRealMethod();

        RequestEntity<?> request = any(RequestEntity.class);
        Class type = any();
        String json = "{\"idProcesso\":\"1\",\"documentos\":{\"1\":\"12\",\"2\":\"14\"}}";
        when(restTemplate.exchange(request, type)).thenReturn(new ResponseEntity(json, HttpStatus.OK));
    }


    @Test
    public void test() throws Exception {
        String ID_PRELIMINAR = "preliminar";
        new ColegiadoEnvironment(List.of("74"),List.of("72","77","73"), ID_PRELIMINAR);

        var processo = new Processo();
        var minuta = new Minuta();
        var pauta = new PedidoInclusaoPauta();
        pauta.setHtmlRenderizado("html renderizado");

        MinutaAnexo relatorio = new MinutaAnexo(1l,null,"html",null,"relatorio",
            minuta,new TipoDocumento("72","",true,true),null,false,1);
        MinutaAnexo voto = new MinutaAnexo(2l,null,"html",null,"voto",
            minuta,new TipoDocumento("77","",true,true),null,false,2);
        MinutaAnexo ementa = new MinutaAnexo(3l,null,"html",null,"ementa",
            minuta,new TipoDocumento("73","",true,true),null,false,3);

        MinutaAnexo preliminar1 = new MinutaAnexo(4l,null,"html",null,"preliminar1",
            minuta,new TipoDocumento(ID_PRELIMINAR,"Uma preliminar",true,true),null,false,0);

        List<MinutaAnexo> acordao = List.of(relatorio,voto,ementa,preliminar1);

        minuta.setAnexos(acordao);
        minuta.setPedidoInclusaoPauta(pauta);
        minuta.setMinutaHtml("acordao concat");
        minuta.setMinutaHtmlRenderizado("acordao concat");
        minuta.setTipoDocumento(new TipoDocumento("74","acordao"));

        processo.setFonteDados(FonteDadosEnum.PJESG);
        processo.setMinutas(Collections.singleton(minuta));
        processo.setIdProcessoSistemaLegado(666l);

        var retorno = service.enviaPauta(processo);
        assertNotNull(retorno, "Deve retornar a minuta");
    }
}
