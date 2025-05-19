package br.jus.tjro.gabinete.scheduled.devolve.origem;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.jus.tjro.gabinete.builders.scheduled.DevolveOrigemBuilder;
import br.jus.tjro.gabinete.builders.scheduled.DevolveOrigemMinutaAnexoBuilder;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DevolveOrigemTest {
    @BeforeAll
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("br.jus.tjro.gabinete.fixtures");
    }

    @Test
    public void enviaUmAnexoParaOrigem() throws Exception {
        DevolveOrigemMinutaAnexo devolve = new DevolveOrigemMinutaAnexoBuilder().get();
        List<MinutaAnexo> anexos = new ArrayList<>();
        MinutaAnexo anexo = Fixture.from(MinutaAnexo.class).gimme("validComAssinatura");
        anexos.add(anexo);
        devolve.devolve(anexos,anexo.getMinutaPai().getProcesso());
    }

    @Test
    public void enviaDoisAnexoParaOrigem() throws Exception {
        DevolveOrigemMinutaAnexo devolve = new DevolveOrigemMinutaAnexoBuilder().get();
        List<MinutaAnexo> anexos = new ArrayList<>();
        anexos.add(Fixture.from(MinutaAnexo.class).gimme("validComAssinatura"));
        anexos.add(Fixture.from(MinutaAnexo.class).gimme("validComAssinatura"));
        devolve.devolve(anexos,anexos.stream().findAny().get().getMinutaPai().getProcesso());
    }

    @Test
    public void enviaDoisAnexoParaOrigemEhDaErroNoPje() throws Exception {
        assertThrows(Exception.class, () -> {
            DevolveOrigemMinutaAnexo devolve = new DevolveOrigemMinutaAnexoBuilder().get();
            List<MinutaAnexo> anexos = new ArrayList<>();
            anexos.add(Fixture.from(MinutaAnexo.class).gimme("validComAssinatura"));
            anexos.add(Fixture.from(MinutaAnexo.class).gimme("validComAssinatura"));
            MinutaAnexo anexoErro = Fixture.from(MinutaAnexo.class).gimme("validComAssinatura");
            anexoErro.setContentType("erro");
            anexos.add(anexoErro);
            devolve.devolve(anexos,anexos.stream().findAny().get().getMinutaPai().getProcesso());
        });
    }
}
