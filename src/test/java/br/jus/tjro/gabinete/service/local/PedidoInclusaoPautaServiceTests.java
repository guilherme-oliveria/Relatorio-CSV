package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.util.AuthenticationUtil;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(value = { SpringExtension.class })
@SpringBootTest(properties = { "pedido.inclusao.pauta.template=" })
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
public class PedidoInclusaoPautaServiceTests {

    @Autowired
    PedidoInclusaoPautaService service;

    @Test
    public void deveResolverOLayout() throws Exception {
        String template = service.resolveTemplate();
        assertEquals("", template);
    }
}
