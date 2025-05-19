package br.jus.tjro.gabinete.service.local;

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.jus.tjro.gabinete.builders.service.BuilderProcessoDocumentoService;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoDocumento;
import br.jus.tjro.gabinete.service.local.processo.documento.ProcessoDocumentoService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProcessoDocumentoServiceTest {
    @BeforeAll
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("br.jus.tjro.gabinete.fixtures");
    }

    @Test
    public void simulaImportacaoDocumento() throws Exception {
        BuilderProcessoDocumentoService builder = new BuilderProcessoDocumentoService();
        ProcessoDocumentoService service = builder.get();

        Processo processo = new Processo();
        processo.setSistema(FonteDadosEnum.PJEPG);
        processo.setId(1l);
        processo.setIdProcessoSistemaLegado(1l);

        List<ProcessoDocumento> retorno = service.importaOuAtualizaDocumentosProcesso(processo,0);
        assertEquals(retorno.size(), 0);
    }

    @Test
    public void deveImportarDoisDocumentosPjeSg() throws Exception {
        BuilderProcessoDocumentoService builder = new BuilderProcessoDocumentoService();
        ProcessoDocumentoService service = builder.get();

        Processo processo = new Processo();
        processo.setSistema(FonteDadosEnum.PJESG);
        processo.setId(2l);
        processo.setIdProcessoSistemaLegado(2l);

        List<ProcessoDocumento> retorno = service.importaOuAtualizaDocumentosProcesso(processo,0);
        assertEquals(retorno.size(), 2);
    }

    @Test
    public void identificaTipoNullNaoErro() throws Exception {
        BuilderProcessoDocumentoService builder = new BuilderProcessoDocumentoService();
        ProcessoDocumentoService service = builder.get();
        ProcessoDocumento documento = new ProcessoDocumento();
        service.identificaTipo(documento);
    }

    @Test
    public void identificaTipoPdf() throws Exception {
        BuilderProcessoDocumentoService builder = new BuilderProcessoDocumentoService();
        ProcessoDocumentoService service = builder.get();
        ProcessoDocumento documento = new ProcessoDocumento();
        service.identificaTipo(documento);
        assertNull(documento.getExtensao());
        documento.setExtensao("pdf");
        service.identificaTipo(documento);
        assertEquals("application/pdf",documento.getExtensao());
    }
}
