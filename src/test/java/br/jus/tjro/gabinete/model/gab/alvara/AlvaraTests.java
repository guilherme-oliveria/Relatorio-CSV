package br.jus.tjro.gabinete.model.gab.alvara;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static br.jus.tjro.gabinete.Tarefas.core.TarefaEnum.*;
import static org.junit.jupiter.api.Assertions.*;

public class AlvaraTests {

    @BeforeAll
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("br.jus.tjro.gabinete.fixtures");
    }


    private Set<Minuta> getMinutaComAlvara() {
        return getMinutaComAlvara(false);
    }

    private Set<Minuta> getMinutaComAlvara(boolean semAssinatura) {
        final Minuta minuta = Fixture.from(Minuta.class).gimme("validMinutarComAssinaturaComAnexoComAssinatura");
        if(!semAssinatura) {
            minuta.setIdMinutaSistemaLegado("666");
            minuta.getAnexos().stream().forEach(p -> p.setIdMinutaSistemaLegado(RandomStringUtils.randomAlphabetic(5)));
        }
        var pagamento = List.of(new PagamentoAlvara("Paulo Jorge", "76291790220",
            new Conta("001", "98756", "", "369258147", "5","",0L),
            "T", true, BigDecimal.valueOf(120),new ContaJudicial("Fulano","666",
            "2290-x", "6549832", "3", "104", "040",
            Double.valueOf("100"), Double.valueOf("120"), new Saldo(Double.valueOf("120"), new Date()), "10/09/2019",
            Optional.of(OrigemDepositoEnum.BACENJUD)), false, "0001", null, null, null, null,null));
        final var alvara = new Alvara(minuta,pagamento,"windson","666.333");
        var set = new HashSet<Minuta>();
        set.add(minuta);
        return set;
    }

    public void integraAlvara(Alvara alvara){
        alvara.getPagamentoAlvara().stream().forEach(p -> p.setEnviado(true));
    }

    @Test
    public void alvaraIntegrado(){
        var alvara = getMinutaComAlvara().stream().findAny().get().getAlvara();
        assertEquals(false,alvara.foiIntegrado());
        integraAlvara(alvara);
        assertEquals(true,alvara.foiIntegrado());
    }

    @Test
    public void enviaAlvaraDepoisDeIntegrado() throws Exception {
        Processo processo = new Processo();
        processo.setMinutas(getMinutaComAlvara());
        processo.paraIntegracao();
        processo.naoConcluso();
        assertEquals(processo.getTarefa(),ParaIntegracaoAlvara);
    }

    @Test
    public void enviaAlvaraDepoisDeIntegradoComErro() throws Exception {
        Processo processo = new Processo();
        processo.setMinutas(getMinutaComAlvara());
        processo.paraIntegracaoComErro();
        processo.naoConcluso();
        assertEquals(processo.getTarefa(),ParaIntegracaoAlvara);
    }

    @Test
    public void naoEnviaAlvaraParaIntegracaoSemManifestacao() throws Exception {
        Processo processo = new Processo();
        processo.setMinutas(getMinutaComAlvara());
        processo.paraIntegracaoSemManifestacao();
        assertEquals(processo.getTarefa(),ParaIntegracaoSemManifestacao);
        processo.naoConcluso();
        assertEquals(processo.getTarefa(),NaoConcluso);
    }

    @Test
    public void colocaNaoConclusoAposIntegrarAlvara() throws Exception {
        Processo processo = new Processo();
        processo.setMinutas(getMinutaComAlvara());
        processo.paraIntegracao();
        processo.naoConcluso();
        assertEquals(processo.getTarefa(),ParaIntegracaoAlvara);
        integraAlvara(processo.getMinutaComAlvaraParaIntegracao().get().getAlvara());
        processo.naoConcluso();
        assertEquals(processo.getTarefa(),NaoConcluso);
    }

    @Test
    public void testaDuasMinutasComDoisAlvarasPendentes() {
        Processo processo = new Processo();
        assertThrows(IllegalStateException.class, () -> {
            var minuta1 = getMinutaComAlvara();
            minuta1.addAll(getMinutaComAlvara());
            processo.setMinutas(minuta1);
            processo.getMinutaComAlvaraParaIntegracao();
        });
    }

    @Test
    public void tentaIntegrarAlvaraComMinutaSemIntegracao() {
        assertThrows(IllegalStateException.class, () -> {
            Processo processo = new Processo();
            processo.setMinutas(getMinutaComAlvara(true));
            processo.getMinutaComAlvaraParaIntegracao();
        });
    }




    @Test
    public void deveTerDoisSacadores() {
        var contaJud = new ContaJudicial("Fulano","666",
            "2290-x", "6549832", "3", "104", "040",
            Double.valueOf("100"), Double.valueOf("120"), new Saldo(Double.valueOf("120"), new Date()), "10/09/2019",
            Optional.of(OrigemDepositoEnum.BACENJUD));
        var pagamento = new PagamentoAlvara("Paulo Jorge", "76291790220",
            new Conta("001", "98756", "", "369258147", "5","",0L),
            "A", true, BigDecimal.valueOf(120), contaJud, false, "0001", "Sacadoido-1", "docSacadoido1", "Sacadoido2", "docSacaoido2",null);

        Long idOrgaoJulgadorSdsg = 666L;
        Processo processo = new Processo();

        Requisitante requisitante = new Requisitante("windson", "123123123");
        Requisitante autorizador = new Requisitante("gedson", "12332132112");
        pagamento.setId(666l);
        GerarAlvara21Request request = new GerarAlvara21Request(pagamento, idOrgaoJulgadorSdsg, processo, requisitante, autorizador, 30, LocalDateTime.now());

        assertEquals(request.getNomeSacador(), pagamento.getNomeSacador());
        assertEquals(request.getNomeSacador2(), pagamento.getNomeSacador2());
        assertEquals(request.getDocumentoSacador(), pagamento.getDocumentoSacador());
        assertEquals(request.getDocumentoSacador2(), pagamento.getDocumentoSacador2());
    }

    @Test
    public void deveTerUMSacador() {
        var pagamento = new PagamentoAlvara("Paulo Jorge", "76291790220",
            new Conta("001", "98756", "", "369258147", "5","",0L),
            "A", true, BigDecimal.valueOf(120),new ContaJudicial("Fulano","666",
            "2290-x", "6549832", "3", "104", "040",
            Double.valueOf("100"), Double.valueOf("120"), new Saldo(Double.valueOf("120"), new Date()), "10/09/2019", Optional.of(OrigemDepositoEnum.BACENJUD)), false, "0001", "Sacadoido-1", "docSacadoido1", null, null, null);

        Long idOrgaoJulgadorSdsg = 666L;
        Processo processo = new Processo();

        Requisitante requisitante = new Requisitante("windson", "123123123");
        Requisitante autorizador = new Requisitante("gedson", "12332132112");
        pagamento.setId(666l);
        GerarAlvara21Request request = new GerarAlvara21Request(pagamento, idOrgaoJulgadorSdsg, processo, requisitante, autorizador,30, LocalDateTime.now());

        assertEquals(request.getNomeSacador(), pagamento.getNomeSacador());
        assertEquals(request.getDocumentoSacador(), pagamento.getDocumentoSacador());
        assertNull(request.getNomeSacador2());
        assertNull(request.getDocumentoSacador2());
    }

    @Test
    public void DeveUsarOBeneficiarioComoSacadorQuandoNaoForEspecificado() {
        var pagamento = new PagamentoAlvara("Paulo Jorge", "76291790220",
            new Conta("001", "98756", "", "369258147", "5", "",0L),
            "A", true, BigDecimal.valueOf(120),new ContaJudicial("Fulano","666",
            "2290-x", "6549832", "3", "104", "040",
            Double.valueOf("100"), Double.valueOf("120"), new Saldo(Double.valueOf("120"), new Date()), "10/09/2019", Optional.of(OrigemDepositoEnum.BACENJUD)), false, "0001", null, null, null, null, null);

        Long idOrgaoJulgadorSdsg = 666L;
        pagamento.setId(666l);
        Processo processo = new Processo();

        Requisitante requisitante = new Requisitante("windson", "123123123");
        Requisitante autorizador = new Requisitante("gedson", "12332132112");
        GerarAlvara21Request request = new GerarAlvara21Request(pagamento, idOrgaoJulgadorSdsg, processo, requisitante, autorizador,30, LocalDateTime.now());

        assertEquals(request.getNomeSacador(), pagamento.getNomeFavorecido());
        assertEquals(request.getDocumentoSacador(), pagamento.getDocFavorecido());
        assertNull(request.getNomeSacador2());
        assertNull(request.getDocumentoSacador2());
    }


    @Test
    public void PodeTerSacador2SomenteNaModalidadeSaque() {
        var pagamento = new PagamentoAlvara("Paulo Jorge", "76291790220",
            new Conta("001", "98756", "", "369258147", "5", "",0L),
            "T", true, BigDecimal.valueOf(120),new ContaJudicial("Fulano","666",
            "2290-x", "6549832", "3", "104", "040",
            Double.valueOf("100"), Double.valueOf("120"), new Saldo(Double.valueOf("120"), new Date()), "10/09/2019", Optional.of(OrigemDepositoEnum.BACENJUD)), false, "0001", "Sacadoido-1", "docSacadoido1", "Sacadoido2", "docSacaoido2", null);

        Long idOrgaoJulgadorSdsg = 666L;
        pagamento.setId(666l);
        Processo processo = new Processo();

        Requisitante requisitante = new Requisitante("windson", "123123123");
        Requisitante autorizador = new Requisitante("gedson", "12332132112");
        GerarAlvara21Request request = new GerarAlvara21Request(pagamento, idOrgaoJulgadorSdsg, processo, requisitante, autorizador, 30, LocalDateTime.now());

        assertNull(request.getNomeSacador2());
        assertNull(request.getDocumentoSacador2());

    }
}
