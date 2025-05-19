package br.jus.tjro.gabinete.model.gab.alvara;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.service.alvara.AlvaraService;
import br.jus.tjro.gabinete.service.local.MinutaService;
import br.jus.tjro.gabinete.service.local.ProcessoService;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.Test;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(value = { SpringExtension.class })
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
public class PagamentoAlvaraTests {

    @BeforeAll
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("br.jus.tjro.gabinete.fixtures");
    }

    @Autowired
    private AlvaraService alvaraService;

    @Autowired
    private MinutaService minutaService;

    @Autowired
    private ProcessoService processoService;

    @Autowired
    private ProcessosRepository processosRepository;

    @Test
    @Transactional
    public void DeveSalvarPagamentosAlvara() throws Exception {
        Processo processo = Fixture.from(Processo.class).gimme("validMinutar");
        processo.setMinutas(Collections.emptySet());
        processo = this.processoService.save(processo);

        Minuta minuta = Fixture.from(Minuta.class).gimme("validMinutarSemAssinatura");
        minuta.setProcesso(processo);
        minuta = minutaService.save(minuta);

        processo.adicionaMinuta(minuta);
        processo = this.processoService.save(processo);

        assertNotNull(minuta.getId());
        assertNotNull(minuta.getProcesso().getId());

        Minuta minutaLoaded = minutaService.pegaUltimaMinutaByProcesso(minuta.getIdProcesso());
        assertEquals(minuta.getId(), minutaLoaded.getId());

        PagamentoAlvara pagamentoAlvara = Fixture.from(PagamentoAlvara.class).gimme("valid");
        PagamentoAlvara pagamentoAlvara2 = Fixture.from(PagamentoAlvara.class).gimme("valid");
        Alvara alvara = Fixture.from(Alvara.class).gimme("valid");
        alvara.setPagamentoAlvara(List.of(pagamentoAlvara, pagamentoAlvara2));
        Alvara saved = alvaraService.salvarInformacoesBancariasAlvara(alvara);
        assertEquals(saved.getPagamentoAlvara().size(), 2);
    }
}
