package br.jus.tjro.gabinete.model.gab.processo;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.UnitTest;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.tag.Tag;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import java.util.*;

import static br.jus.tjro.gabinete.Tarefas.core.TarefaEnum.*;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProcessoTest {

    private final ProcessoTestDSL processosComTarefasIntermediariasOuNaoConcluso = nasTarefas(NaoConcluso, Assinando, ParaIntegracaoSemManifestacao, ParaIntegracao, ParaIntegracaoComErro)
            .dadoQue(tarefa -> {
                final Processo processo = new Processo();
                try {
                    processo.setTarefa(tarefa);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return processo;
            });

    @Test
    public void testa_devolucao_sem_manifestacao_para_processo_nao_concluso() {
        processosComTarefasIntermediariasOuNaoConcluso
            .deveLancar(IllegalStateException.class)
            .quando(processo -> processo.devolveOrigemSemManifestacao());
    }

    @Test
    public void testa_mudanca_para_devolucao_para_origem_para_todas_as_tarefas_intermediarias() {
        processosComTarefasIntermediariasOuNaoConcluso
            .deveLancar(IllegalStateException.class)
            .quando(processo -> processo.paraIntegracaoSemManifestacao());
    }

    @Test
    public void testa_recusa_de_processo() throws Exception {
        final Processo processo = new Processo();
        processo.setTarefa(TarefaEnum.Assinar);
        processo.recusar();
        assertThat(processo.getTarefa()).isEqualTo(TarefaEnum.Corrigir);
    }

    @Test
    public void testa_recusa_de_processo_que_nao_esta_para_assinar() throws Exception {
        final Processo processo = new Processo();
        processo.setTarefa(TarefaEnum.Minutar);

        assertThatThrownBy(() -> processo.recusar()).
            isInstanceOf(IllegalStateException.class).
            hasMessage("Processo não pode ser recusado pois não estava para Assinar");
    }


    @Test
    public void verificaEstadoTarefaDoProcessoInstanciarobjeto(){
        assertNull(new Processo().getTarefa());
    }

    @Test
    public void verificaProcessoSomenteComUmaMinutaIntegrado(){
        Processo processo = new Processo();
        Set<Minuta> minutas = new HashSet<Minuta>();
        minutas.add(getMinutaIntegrada());
        processo.setMinutas(minutas);
        assertTrue(processo.minutaFoiIntegrado());
    }

    @Test
    public void verificaProcessoComVariasMinutaIntegrado(){
        Processo processo = new Processo();
        Set<Minuta> minutas = new HashSet<Minuta>();
        minutas.add(getMinutaIntegrada());
        minutas.add(getMinutaIntegrada());
        processo.setMinutas(minutas);
        assertTrue(processo.minutaFoiIntegrado());
    }

    @Test
    public void verificaProcessoComUmaMinutaNaoIntegrada(){
        Processo processo = new Processo();
        Set<Minuta> minutas = new HashSet<Minuta>();
        minutas.add(getMinutaNaoIntegrada());
        processo.setMinutas(minutas);
        assertFalse(processo.minutaFoiIntegrado());
    }

    @Test
    public void verificaProcessoComVariasMinutaNaoIntegrada(){
        Processo processo = new Processo();
        Set<Minuta> minutas = new HashSet<Minuta>();
        minutas.add(getMinutaNaoIntegrada());
        minutas.add(getMinutaNaoIntegrada());
        processo.setMinutas(minutas);
        assertFalse(processo.minutaFoiIntegrado());
    }

    @Test
    public void verificaProcessoComVariasMinutasIntegradaENaoIntegrada(){
        Processo processo = new Processo();
        Set<Minuta> minutas = new HashSet<Minuta>();
        minutas.add(getMinutaIntegrada());
        minutas.add(getMinutaNaoIntegrada());
        minutas.add(getMinutaIntegrada());
        processo.setMinutas(minutas);
        assertFalse(processo.minutaFoiIntegrado());
    }

    @Test
    public void testaProcessoIntegradoComMinutaNull(){
        Processo processo = new Processo();
        ArrayList<Minuta> minutas = new ArrayList<Minuta>();
        assertTrue(processo.minutaFoiIntegrado());
    }

    @Test
    public void testaProcessoIntegradoComMinutaSizeZero(){
        Processo processo = new Processo();
        ArrayList<Minuta> minutas = new ArrayList<Minuta>();
        assertTrue(processo.minutaFoiIntegrado());
    }

    @Test
    public void naoConclusoSincronizacao() throws Exception {
        Processo processo = new Processo();
        processo.setTarefa(Assinar);
        processo.adicionaMinuta(new Minuta());
        processo.naoConcluso();
        assertEquals(NaoConcluso, processo.getTarefa());
    }


    @Test
    public void processoContinuaNaoConclusoPoisNaoFoiIntegrado() throws Exception {
        Processo processo = new Processo();
        processo.setTarefa(Assinar);
        processo.adicionaMinuta(new Minuta());
        processo.naoConcluso();
        assertEquals(NaoConcluso, processo.getTarefa());
    }

    @Test
    public void adicionaMinutaIntegrada() {
        assertThrows(IllegalArgumentException.class, () -> {
            Processo processo = new Processo();
            processo.adicionaMinuta(getMinutaIntegrada());
        });
    }

    @Test
    public void adicionaDuasMinutasIguaisDeveRetornarSoUma() throws Exception {
        Processo processo = new Processo();
        Minuta minuta1 = new Minuta();
        minuta1.setId(1l);
        Minuta minuta2 = new Minuta();
        minuta2.setId(1l);
        processo.adicionaMinuta(minuta1);
        processo.adicionaMinuta(minuta2);
        assertEquals(1,processo.getMinutas().size());
    }


    @Test
    public void adicionaMinutaComMinutaJaEmElaboracao() {
        assertThrows(IllegalStateException.class, () -> {
            Processo processo = new Processo();
            Minuta minuta1 = new Minuta();
            minuta1.setId(1l);
            Minuta minuta2 = new Minuta();
            minuta2.setId(2l);
            processo.adicionaMinuta(minuta1);
            processo.adicionaMinuta(minuta2);
        });
    }

    @Test
    public void testaProcessoPegaUltimaMinutaProcesso() throws Exception {
        Processo processo = new Processo();
        Minuta minuta = getMinutaNaoIntegrada();
        processo.adicionaMinuta(minuta);
        assertEquals(minuta,processo.minutaEmElaboracao().get());
    }

    @Test
    public void removeConclusaoProcesso() throws Exception {
        Processo processo = new Processo();
        Minuta minuta = new Minuta();
        processo.adicionaMinuta(minuta);
        minuta.setIdMinutaSistemaLegado("666");
        processo.paraIntegracao();
        processo.naoConcluso();
        assertEquals(NaoConcluso,processo.getTarefa());
        assertTrue(processo.minutaFoiIntegrado());
    }

    @Test
    public void removeConclusaoProcessoSemManifestacao() throws Exception {
        Processo processo = new Processo();
        processo.adicionaMinuta(getMinutaNaoIntegrada());
        processo.paraIntegracaoSemManifestacao();
        processo.naoConcluso();
        assertEquals(NaoConcluso,processo.getTarefa());
    }

    @Test
    public void procuraMinutaEmElaboracaoSemMinuta() throws Exception {
        Processo processo = new Processo();
        Optional<Minuta> minuta = processo.minutaEmElaboracao();
        assertFalse(minuta.isPresent());
    }

    @Test
    public void devolveOrigemComMinutaAtivaMantemMesmaTarefa() throws Exception {
        Processo processo = new Processo();
        processo.adicionaMinuta(new Minuta());
        processo.paraIntegracao();
        assertFalse(processo.minutaFoiIntegrado());
        assertEquals(ParaIntegracao,processo.getTarefa());
        processo.naoConcluso();
        assertEquals(ParaIntegracao,processo.getTarefa());
    }

    @Test
    public void devolveOrigemComMinutaAtivaMantemMesmaTarefa2() throws Exception {
        Processo processo = new Processo();
        processo.adicionaMinuta(new Minuta());
        processo.paraIntegracaoComErro();
        assertFalse(processo.minutaFoiIntegrado());
        assertEquals(ParaIntegracaoComErro,processo.getTarefa());
        processo.naoConcluso();
        assertEquals(ParaIntegracaoComErro,processo.getTarefa());
    }

    @Test
    public void evitaNullPointerEmManifestacao() throws Exception {
        Processo processo = new Processo();
        assertEquals(0, processo.getManifestacao());
    }

    @Test
    public void testaNumeroProcessoSemFormatacao() throws Exception {
        Processo processo = new Processo();
        processo.setNumeroProcesso("7030378-95.2018.8.22.0001");
        assertEquals("7030378-95.2018.8.22.0001",processo.getNumeroProcesso());
        assertEquals("70303789520188220001",processo.numeroProcessoSemFormatacao());
    }

    @Test
    public void atualizaDataSincronizacaoErro(){
        Processo processo = new Processo();
        processo.atualizaDataSincronizacaoErro(new Date());
        assertEquals(1, processo.getErroSincronizacao());
        processo.atualizaDataSincronizacaoSucesso(new Date());
        assertEquals(0, processo.getErroSincronizacao());
    }

    @Test
    public void removeProcessoTags() throws Exception {
        Processo processo = new Processo();
        processo.devolveOrigemSemManifestacao();
        List<ProcessoTag> tags = List.of(
            new ProcessoTag(new Tag(),true,processo),
            new ProcessoTag(new Tag(),false,processo));
        processo.setTags(tags);
        assertEquals(2,processo.getTags().stream().filter(p->p.getDataExclusao() == null).collect(toList()).size());
        processo.naoConcluso();
        assertEquals(1,processo.getTags().stream().filter(p->p.getDataExclusao() == null).collect(toList()).size());
    }

    @Test
    public void processoIntegracaoPautaParaNaoConcluso() throws Exception {
        Processo processo = new Processo();
        processo.paraIntegracaoPauta();
        assertEquals(ParaIntegracaoPauta,processo.getTarefa());
        processo.naoConcluso();
        assertEquals(NaoConcluso,processo.getTarefa());
    }

    private Minuta getMinutaIntegrada(){
        Minuta minuta = mock(Minuta.class);
        when(minuta.foiIntegrada()).thenReturn(true);
        return minuta;
    }

    private Minuta getMinutaNaoIntegrada(){
        Minuta minuta = mock(Minuta.class);
        when(minuta.foiIntegrada()).thenReturn(false);
        return minuta;
    }

    private ProcessoTestDSL nasTarefas(TarefaEnum... tarefas) {
        return new ProcessoTestDSL(tarefas);
    }
}
