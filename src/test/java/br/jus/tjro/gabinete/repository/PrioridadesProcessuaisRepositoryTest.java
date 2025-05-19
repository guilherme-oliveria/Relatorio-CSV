package br.jus.tjro.gabinete.repository;

import br.jus.tjro.gabinete.model.gab.processo.PrioridadeProcessual;
import br.jus.tjro.gabinete.repository.gab.processo.PrioridadesProcessuaisRepository;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
public class PrioridadesProcessuaisRepositoryTest {

    @Autowired
    private PrioridadesProcessuaisRepository prioridadesProcessuaisRepository;

    private final PrioridadeProcessual prioridade = new PrioridadeProcessual();

    @BeforeEach
    public void setUpBefore() {
        prioridade.setId(1);
        prioridade.setDescricao("Idoso(a)");
        prioridade.setIdadeMinima(60);
        this.prioridadesProcessuaisRepository.save(prioridade);

        PrioridadeProcessual prioridadeProcessual = new PrioridadeProcessual();
        prioridadeProcessual.setId(2);
        prioridadeProcessual.setDescricao("Réu Preso");
        prioridadeProcessual.setIdadeMinima(0);
        this.prioridadesProcessuaisRepository.save(prioridadeProcessual);
    }

    @AfterEach
    public void tearDownAfter() {
        this.prioridadesProcessuaisRepository.deleteAll();
    }

    @Test
    public void deveBuscarListaDeCaixasDadoListaTarefas() {
        PrioridadeProcessual prioridadePro = this.prioridadesProcessuaisRepository.findById(2).get(); //ToDo testas o NullPointer

        assertNotNull(prioridadePro);
        assertEquals(prioridadePro.getDescricao(), "Réu Preso");
    }
}
