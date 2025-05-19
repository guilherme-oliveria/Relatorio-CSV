package br.jus.tjro.gabinete.model.gab;

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.jus.tjro.gabinete.UnitTest;
import br.jus.tjro.gabinete.model.gab.proxy.TipoDocumentoProxyHandler;
import br.jus.tjro.gabinete.service.local.TipoDocumentoService;
import org.jboss.resteasy.spi.ApplicationException;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TipoDocumentoProxyHandlerTest {

    @BeforeAll
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("br.jus.tjro.gabinete.fixtures");
    }

    @Test
    public void verificaRecuperarObjeto() throws IllegalAccessException, InstantiationException {
        TipoDocumentoService service = mock(TipoDocumentoService.class);
        when(service.findById(any())).thenReturn(Optional.of(new TipoDocumento("1", "windson", true, true)));

        TipoDocumentoProxyHandler handler = new TipoDocumentoProxyHandler(service,"1");

        TipoDocumento tipoDocumento = handler.criaProxy();

        assertNotNull(tipoDocumento.getId());
        assertNotNull(tipoDocumento.getDescricao());
        assertNotNull(tipoDocumento.getDescricao());
        verify(service,atMost(1)).findById(any());
        verify(service,atLeast(1)).findById(any());
    }

    @Test
    public void chamadaGetId(){
        TipoDocumentoService service = mock(TipoDocumentoService.class);
        TipoDocumentoProxyHandler handler = new TipoDocumentoProxyHandler(service,"1");
        TipoDocumento tipoDocumento = handler.criaProxy();
        assertEquals("1",tipoDocumento.getId());
        verify(service,atMost(0)).findById(any());
        verify(service,atLeast(0)).findById(any());
    }

    @Test
    public void criaProxyComIdNullRetornaGetIdNull() throws IllegalAccessException, InstantiationException {
        TipoDocumentoService service = mock(TipoDocumentoService.class);
        TipoDocumentoProxyHandler handler = new TipoDocumentoProxyHandler(service,null);
        TipoDocumento tipoDocumento = handler.criaProxy();
        assertNull(tipoDocumento.getId());
    }

    @Test
    public void criaProxyComServiceNull(){
        assertThrows(IllegalArgumentException.class, () -> new TipoDocumentoProxyHandler(null,"1"));
    }

    @Test
    public void chamadaDeEquals(){
        TipoDocumentoService service = mock(TipoDocumentoService.class);
        TipoDocumentoProxyHandler handler = new TipoDocumentoProxyHandler(service,"1");
        TipoDocumentoProxyHandler handlerComparacao = new TipoDocumentoProxyHandler(service,"1");
        TipoDocumento tipoDocumento = handler.criaProxy();
        TipoDocumento tipoDocumentoComparacao = handlerComparacao.criaProxy();
        assertTrue(tipoDocumento.equals(tipoDocumentoComparacao));
        assertTrue(tipoDocumentoComparacao.equals(tipoDocumento));
        verify(service,atMost(0)).findById(any());
        verify(service,atLeast(0)).findById(any());
    }

    @Test
    public void chamdaDeObjetoNull(){
        TipoDocumentoService service = mock(TipoDocumentoService.class);
        final TipoDocumentoProxyHandler handler = new TipoDocumentoProxyHandler(service, null);
        TipoDocumento tipoDocumento = handler.criaProxy();

        assertNull(tipoDocumento.getId());
    }
}
