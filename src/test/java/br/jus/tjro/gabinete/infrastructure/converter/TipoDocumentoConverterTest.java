package br.jus.tjro.gabinete.infrastructure.converter;

import br.jus.tjro.gabinete.config.ColegiadoEnvironment;
import br.jus.tjro.gabinete.config.ColegiadoEnvironmentTests;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;
import br.jus.tjro.gabinete.service.local.TipoDocumentoService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TipoDocumentoConverterTest {


    @Test
    public void testaQuandoColunaEhNull(){
        TipoDocumentoConverter converter = new TipoDocumentoConverter();
        converter.init(mock(TipoDocumentoService.class),null);
        TipoDocumento retorno = converter.convertToEntityAttribute(null);
        assertNull(retorno.getId());
    }

    @Test
    public void testaQuandoMudaId(){
        TipoDocumentoService service = mock(TipoDocumentoService.class);
        when(service.findById("1")).
            thenReturn(Optional.of(new TipoDocumento("1", "windson", true, true)));
        when(service.findById("2")).
            thenReturn(Optional.of(new TipoDocumento("2", "jedeson", true, true)));

        TipoDocumentoConverter converter = new TipoDocumentoConverter();
        converter.init(service,null);
        TipoDocumento retorno = converter.convertToEntityAttribute(null);
        assertNull(retorno.getId());

        retorno = converter.convertToEntityAttribute("1");
        assertEquals("1",retorno.getId());
        assertEquals("windson",retorno.getDescricao());


        retorno = converter.convertToEntityAttribute("2");
        assertEquals("2",retorno.getId());
        assertEquals("jedeson",retorno.getDescricao());
    }

    @Test
    public void testaTipoDocumentoColegiado(){
        TipoDocumentoService service = mock(TipoDocumentoService.class);
        when(service.findById("1")).
            thenReturn(Optional.of(new TipoDocumento("1", "windson", true, true)));

        TipoDocumentoConverter converter = new TipoDocumentoConverter();
        converter.init(service,new ColegiadoEnvironment(List.of("1"),List.of(), ""));
        TipoDocumento retorno = converter.convertToEntityAttribute("1");
        assertEquals("1",retorno.getId());
        assertEquals("windson",retorno.getDescricao());
        assertEquals(true,retorno.isMinutaColegiado());
        assertEquals(false,retorno.isAnexoColegiado());
    }

    @Test
    public void testaTipoDocumentoAnexoColegiado(){
        TipoDocumentoService service = mock(TipoDocumentoService.class);
        when(service.findById("1")).
            thenReturn(Optional.of(new TipoDocumento("1", "windson", true, true)));
        TipoDocumentoConverter converter = new TipoDocumentoConverter();
        converter.init(service,new ColegiadoEnvironment(List.of(),List.of("1"), ""));
        TipoDocumento retorno = converter.convertToEntityAttribute("1");
        assertEquals("1",retorno.getId());
        assertEquals("windson",retorno.getDescricao());
        assertEquals(true,retorno.isAnexoColegiado());
        assertEquals(false,retorno.isMinutaColegiado());
    }
}
