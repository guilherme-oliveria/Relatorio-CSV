package br.jus.tjro.gabinete.model.gab.minuta;

import br.jus.tjro.gabinete.Tarefas.TarefaNaoConcluso;
import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.UnitTest;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.localizador.Caixa;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoTag;
import br.jus.tjro.gabinete.model.gab.tag.Tag;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MinutaRecebidaTest {

    @Test()
    public void minutaRecebidaSemConteudoError() {
        assertThrows(Throwable.class, () ->
            new MinutaRecebida(666l, FonteDadosEnum.PJEPG,661l,"")
        );
    }

    @Test()
    public void testaResposta() throws Exception {
        MinutaRecebida minutaRecebida = new MinutaRecebida(666l, FonteDadosEnum.PJEPG,661l,"<h1>windson</h1>");
        minutaRecebida.comErro();
        assertEquals(minutaRecebida.getResposta().getStatus(), MinutaRecebidaStatus.COM_ERRO);
    }

    @Test()
    public void deveAtribuirProcessoComTagDeSistema() throws Exception {
        MinutaRecebida minutaRecebida = new MinutaRecebida(666l, FonteDadosEnum.PJEPG,661l,"<h1>windson</h1>");
        Processo processo = new Processo(666L);
        processo.setTags(new ArrayList<>());
        Optional<ProcessoTag> processoTagSistema = Optional.of(new ProcessoTag(new Tag("windson", "COR", Tag.OrgaoTagTipoSistema), false, processo));
        Optional<ProcessoTag> processoTag = Optional.of(new ProcessoTag(new Tag("windson", "COR", "PJEPG-windson"), false, processo));
        Caixa caixa = new Caixa("caixa validar exp cartorio");
        minutaRecebida.setProcesso(processo, processoTagSistema, caixa);

        assertTrue(processo.getTags().stream().anyMatch(p -> p.equals(processoTagSistema.get())));
        assertThrows(Throwable.class,() -> minutaRecebida.setProcesso(processo, processoTag, caixa));
    }

    @Test()
    public void deveSetarACaixaValidarExpCartorioSeProcessoNaoConcluso() throws Exception {
        MinutaRecebida minutaRecebida = new MinutaRecebida(666l, FonteDadosEnum.PJEPG,661l,"<h1>windson</h1>");
        Processo processo = new Processo(666L);
        processo.setTarefa(TarefaEnum.NaoConcluso);
        processo.setTags(new ArrayList<>());
        Optional<ProcessoTag> processoTagSistema = Optional.of(new ProcessoTag(new Tag("windson", "COR", Tag.OrgaoTagTipoSistema), false, processo));
        Caixa caixaValidarExpCartorio = new Caixa("caixa validar exp cartorio");
        minutaRecebida.setProcesso(processo, processoTagSistema, caixaValidarExpCartorio);
        assertEquals(processo.getCaixa(), caixaValidarExpCartorio);
    }

    @Test()
    public void NAoodeveSetarACaixaValidarExpCartorioSeProcessoNaoConcluso() throws Exception {
        MinutaRecebida minutaRecebida = new MinutaRecebida(666l, FonteDadosEnum.PJEPG,661l,"<h1>windson</h1>");
        Processo processo = new Processo(666L);
        processo.setTarefa(TarefaEnum.Minutar);
        processo.setTags(new ArrayList<>());
        Optional<ProcessoTag> processoTagSistema = Optional.of(new ProcessoTag(new Tag("windson", "COR", Tag.OrgaoTagTipoSistema), false, processo));
        Caixa caixaValidarExpCartorio = new Caixa("caixa validar exp cartorio");
        minutaRecebida.setProcesso(processo, processoTagSistema, caixaValidarExpCartorio);
        assertNotEquals(processo.getCaixa(), caixaValidarExpCartorio);
    }
}

