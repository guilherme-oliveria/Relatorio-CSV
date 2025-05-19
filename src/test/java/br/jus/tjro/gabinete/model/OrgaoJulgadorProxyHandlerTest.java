package br.jus.tjro.gabinete.model;

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.jus.tjro.gabinete.model.gab.proxy.OrgaoJulgadorProxyHandler;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.service.local.OrgaoJulgadorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrgaoJulgadorProxyHandlerTest {

    @BeforeAll
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("br.jus.tjro.gabinete.fixtures");
    }

    @Test
    public void verificaRecuperarObjeto() throws IllegalAccessException, InstantiationException {
        OrgaoJulgadorService service = mock(OrgaoJulgadorService.class);
        when(service.pegaOrgaoJulgadorPorId(any())).thenReturn(new OrgaoJulgador("pjepg-1", "windson", List.of(),"sigla", List.of()));

        OrgaoJulgadorProxyHandler handler = new OrgaoJulgadorProxyHandler(service,"pjepg-1");
        OrgaoJulgadorProxyHandler handlerComparacao = new OrgaoJulgadorProxyHandler(service,"pjepg-1");

        OrgaoJulgador orgaoJulgador = handler.criaProxy();
        OrgaoJulgador orgaoJulgadorComparacao = handlerComparacao.criaProxy();

        assertNotNull(orgaoJulgador.getId());
        assertTrue(orgaoJulgador.equals(orgaoJulgadorComparacao));
        verify(service,atMost(0)).pegaOrgaoJulgadorPorId(any());
        verify(service,atLeast(0)).pegaOrgaoJulgadorPorId(any());
        assertNotNull(orgaoJulgador.getDescricao());
        assertNotNull(orgaoJulgador.getDescricao());
        verify(service,atMost(1)).pegaOrgaoJulgadorPorId(any());
        verify(service,atLeast(1)).pegaOrgaoJulgadorPorId(any());
    }

    @Test
    public void criaProxyComServiceNull(){
        assertThrows(IllegalArgumentException.class, () -> new OrgaoJulgadorProxyHandler(null,"1"));
    }
}
