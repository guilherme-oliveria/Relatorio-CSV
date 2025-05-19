package br.jus.tjro.gabinete.service.local.processo.documento;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.jus.tjro.gabinete.UnitTest;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoDocumento;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoDocumentoRepository;
import br.jus.tjro.gabinete.scheduled.BuscaMovimentosDosProcessos;
import br.jus.tjro.gabinete.service.remoto.ProcessoDocumentoRemotoService;

import java.util.List;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class ProcessoDocumentoServiceTest {

    @BeforeAll
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("br.jus.tjro.gabinete.fixtures");
    }

    @Test
    public void deveMontarObjProcessoDocumento() {
        ProcessoDocumentoRemotoService processoDocumentoRemotoService = mock(ProcessoDocumentoRemotoService.class);
        ProcessoDocumentoRepository processoDocumentoRepository = mock(ProcessoDocumentoRepository.class);
        BuscaMovimentosDosProcessos buscaMovimentosDosProcessos = null;
        Processo processo = Fixture.from(Processo.class).gimme("valid");
        TipoDocumento tipoDocumento = getTiposDocs().get(0);

        ProcessoDocumento doc = Fixture.from(ProcessoDocumento.class).gimme("valido");
        doc.setProcesso(processo);
        doc.setTipoDocumento(tipoDocumento);

        ProcessoDocumento processoDocumentoMontado = new ProcessoDocumento(
            doc.getId(), processo, doc.getDescricao(), doc.getDocumentoHtml(),
            doc.getDataJuntada(), doc.getNomeUserInclusao(), doc.getEhSigiloso(),
            doc.getExtensao(), doc.getTipoDocumento(),
            doc.getIdUsuarioExclusao(), doc.getMotivoExclusao(), doc.getDataExclusao(), doc.getAtivo(), doc.getNrOrdem(), doc.getHash());

        assertEquals(doc.getId().toString(),processoDocumentoMontado.getIdDocumentoSistemaLegado());
        assertEquals(null,processoDocumentoMontado.getId());
    }

    private List<TipoDocumento> getTiposDocs(){
        TipoDocumento
            ob = new TipoDocumento("1"),
            ob2 = new TipoDocumento("2");
        return List.of(ob, ob2);
    }
}
