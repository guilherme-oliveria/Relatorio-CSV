package br.jus.tjro.gabinete.scheduled.devolve.origem;

import br.jus.tjro.gabinete.api.modelo.DocumentoApi;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import br.jus.tjro.gabinete.scheduled.devolve.origem.builder.DocumentoBuilderApi;
import br.jus.tjro.gabinete.service.assinaturadigital.RetornoAssinaturaService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class DocumentoBuilderApiTest {
    @Test
    public void criaAnexoComSigilo() throws Exception {
        RetornoAssinaturaService service = mock(RetornoAssinaturaService.class);
        MinutaAnexo anexo = new MinutaAnexo();
        Minuta minuta = new Minuta();
        minuta.setIdMinutaSistemaLegado("osvaldo");
        anexo.setMinutaPai(minuta);
        anexo.setSigilo(true);
        anexo.setTipoDocumento(new TipoDocumento("666"));
        anexo.setId(66l);
        DocumentoBuilderApi instancia = new DocumentoBuilderApi(service);
        DocumentoApi retorno = instancia.parse(anexo);
        assertEquals("gab-66",retorno.getIdModulo());
        assertTrue(retorno.isSigilo());
    }

    @Test
    public void criaAnexoSemSigilo() throws Exception {
        RetornoAssinaturaService service = mock(RetornoAssinaturaService.class);
        MinutaAnexo anexo = new MinutaAnexo();
        anexo.setId(666l);
        anexo.setTipoDocumento(new TipoDocumento("666"));
        Minuta minuta = new Minuta();
        minuta.setIdMinutaSistemaLegado("666");
        anexo.setMinutaPai(minuta);
        anexo.setSigilo(true);
        DocumentoBuilderApi instancia = new DocumentoBuilderApi(service);
        DocumentoApi retorno = instancia.parse(anexo);
        assertEquals("gab-666",retorno.getIdModulo());
        assertTrue(retorno.isSigilo());
    }
}
