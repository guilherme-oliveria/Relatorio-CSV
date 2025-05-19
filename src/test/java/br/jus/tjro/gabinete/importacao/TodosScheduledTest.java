package br.jus.tjro.gabinete.importacao;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.jus.tjro.gabinete.importacao.builder.BuilderTodosScheduled;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.scheduled.TodosScheduled;
import org.junit.jupiter.api.BeforeAll;

public class TodosScheduledTest {

    @BeforeAll
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("br.jus.tjro.gabinete.fixtures");
    }


    public void testaExecucaoTodosScheduled() throws Exception {
        BuilderTodosScheduled builder = new BuilderTodosScheduled();
        Processo processo = getProcesso();
        TodosScheduled todos = builder.get();
        todos.executaPorProcesso(processo);
    }

    private Processo getProcesso() {
        Processo processo = Fixture.from(Processo.class).gimme("paraImportacao");
        return processo;
    }
}
