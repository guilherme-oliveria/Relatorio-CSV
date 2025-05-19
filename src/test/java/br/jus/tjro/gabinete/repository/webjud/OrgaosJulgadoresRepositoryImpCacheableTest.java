package br.jus.tjro.gabinete.repository.webjud;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import io.zonky.test.db.postgres.junit.EmbeddedPostgresRules;
import io.zonky.test.db.postgres.junit.SingleInstancePostgresRule;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.AopTestUtils;

import java.util.ArrayList;
import java.util.List;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(value = { SpringExtension.class })
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
class OrgaosJulgadoresRepositoryImpCacheableTest {

    OrgaosJulgadoresRepositoryImp mock;
    final Usuario stz = new Usuario("Steimntz", "18", of(), "w1nd0N$");
    final List<OrgaoJulgador> orgaos = of(new OrgaoJulgador("Catolé-18"));

    @Autowired
    private OrgaosJulgadoresRepositoryImp impl;

    @BeforeEach
    void setUp() {
        mock = AopTestUtils.getTargetObject(impl);
        reset(mock);

        when(mock.findAllByUsuario(stz))
            .thenReturn(orgaos)
            .thenReturn(new ArrayList<>());
    }

    @Test
    void findAllByUsuario() {
        assertEquals(orgaos, impl.findAllByUsuario(stz));
        verify(mock).findAllByUsuario(stz);

        assertEquals(orgaos, impl.findAllByUsuario(stz));
        assertEquals(orgaos, impl.findAllByUsuario(stz));

        verifyNoMoreInteractions(mock);
    }

    @EnableCaching
    @Configuration
    public static class CachingTestConfig {

        @Bean
        public OrgaosJulgadoresRepository bookRepositoryMockImplementation() {
            return mock(OrgaosJulgadoresRepositoryImp.class);
        }

        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("usuario-orgao-julgador");
        }

    }
}
