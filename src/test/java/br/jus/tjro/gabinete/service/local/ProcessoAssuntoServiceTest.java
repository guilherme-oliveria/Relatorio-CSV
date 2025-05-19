package br.jus.tjro.gabinete.service.local;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoAssunto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProcessoAssuntoServiceTest {
    ProcessoAssuntoService processoAssuntoService = new ProcessoAssuntoService();

    @BeforeAll
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("br.jus.tjro.gabinete.fixtures");
    }

    @Test
    public void deveMontarObjProcessoAssunto() {
        Processo processo = Fixture.from(Processo.class).gimme("valid");
        ProcessoAssunto processoAssunto = Fixture.from(ProcessoAssunto.class).gimme("validoSemId");
        processoAssunto.setProcesso(processo);

        ProcessoAssunto processoAssuntoMontado = processoAssuntoService.montaObjProcessoAssunto(
            processoAssunto.getProcesso(), processoAssunto.getIdAssunto(), processoAssunto.getAssuntoPrincipal());

        assertThat(processoAssunto).isEqualToComparingFieldByField(processoAssuntoMontado);
    }
}
