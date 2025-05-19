package br.jus.tjro.gabinete.model.gab.processo;

import br.jus.tjro.gabinete.UnitTest;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MinutaAnexoTest {
    @Test
    public void testa_anexo_deve() {
        MinutaAnexo anexo = new MinutaAnexo();
        anexo.setIdMinutaSistemaLegado("idMinuta");
        anexo.setHashP7s("Assinatura");
        assertTrue(anexo.foiIntegrado());
    }

    @Test
    public void testa_anexo_nao_integrada() {
        MinutaAnexo anexo = new MinutaAnexo();
        assertFalse(anexo.foiIntegrado());
    }

    @Test
    public void testa_anexo_nao_integrado_porem_assinado() {
        MinutaAnexo anexo = new MinutaAnexo();
        anexo.setHashP7s("Assinatura");
        assertFalse(anexo.foiIntegrado());
    }

    @Test
    public void testa_content_type_null() {
        MinutaAnexo anexo = new MinutaAnexo();
        assertEquals("application/pdf",anexo.getContentType());
    }

    @Test
    public void testa_is_invalid() {
        MinutaAnexo anexo = new MinutaAnexo();
        assertFalse(anexo.isValid());
    }

    @Test
    public void testa_is_colegiado_valid() {
        TipoDocumento tipoDoc = new TipoDocumento("74", "Voto");
        MinutaAnexo anexo = new MinutaAnexo();
        anexo.setMinutaPai(new Minuta(666L));
        anexo.setTipoDocumento(tipoDoc);
        anexo.setHtml("windson");
        anexo.setDescricao("windson-doc");
        assertTrue(anexo.isValid());
    }

    @Test
    public void testa_is_monocratica_is_valid() {
        TipoDocumento tipoDoc = new TipoDocumento("74", "Voto");
        MinutaAnexo anexo = new MinutaAnexo();
        anexo.setMinutaPai(new Minuta(666L));
        anexo.setTipoDocumento(tipoDoc);
        anexo.setHash("windsonshah");
        anexo.setDescricao("windson-doc");
        assertTrue(anexo.isValid());
    }
}

