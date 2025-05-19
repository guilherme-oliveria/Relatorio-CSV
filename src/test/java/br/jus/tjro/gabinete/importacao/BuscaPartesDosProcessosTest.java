package br.jus.tjro.gabinete.importacao;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.importacao.builder.BuilderBuscaPartesDosProcessos;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.scheduled.BuscaPartesDosProcessos;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuscaPartesDosProcessosTest {

    @BeforeAll
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("br.jus.tjro.gabinete.fixtures");
    }

    @Test
    public void testaBuscaPartesProcesso() throws Exception {
        Processo processo = Fixture.from(Processo.class).gimme("paraImportacao");
        processo.setSistema(FonteDadosEnum.PJEPG);
        BuilderBuscaPartesDosProcessos builder = new BuilderBuscaPartesDosProcessos();
        BuscaPartesDosProcessos buscaPartesDosProcessos = builder.get();
        Boolean resultado = buscaPartesDosProcessos.executaPorProcesso(processo);
        assertEquals(true,resultado);
    }

    @Test()
    public void testaReturnFalseBuscaPartesProcesso() throws Exception {
        Processo processo = Fixture.from(Processo.class).gimme("paraImportacao");
        processo.setId(-1l);
        BuilderBuscaPartesDosProcessos builder = new BuilderBuscaPartesDosProcessos();
        BuscaPartesDosProcessos buscaPartesDosProcessos = builder.get();
        Boolean resultado = buscaPartesDosProcessos.executaPorProcesso(processo);
        assertEquals(false,resultado);
    }
}
