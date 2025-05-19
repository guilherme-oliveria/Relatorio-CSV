package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaRecebida;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaRecebidaStatus;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.minuta.MinutasRecebidasRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static br.jus.tjro.gabinete.Tarefas.core.TarefaEnum.Minutar;
import static br.jus.tjro.gabinete.Tarefas.core.TarefaEnum.NaoConcluso;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
public class MinutaRecebidaServiceTest {

    @Test
    public void deve_setar_tarefa_minutar_e_status_finalizada() throws Exception {
        Usuario windson = new Usuario("windson", "1", Collections.emptyList(), "tchoken");
        MinutaService minutaService = mock(MinutaService.class);
        MinutasRecebidasRepository minutasRecebidasRepository = mock(MinutasRecebidasRepository.class);
        ProcessosRepository processosRepository = mock(ProcessosRepository.class);
        MinutaTarefaLogService minutaTarefaLogService = mock(MinutaTarefaLogService.class);
        Processo processo = new Processo(666l);
        Minuta minuta = new Minuta(processo);
        processo.adicionaMinuta(minuta);
        Minuta novaMinuta = new Minuta(processo);
        MinutaRecebida minutaRecebida = new MinutaRecebida(6666l, FonteDadosEnum.PJEPG, 666l, "<h1>windson</h1>");
        minutaRecebida.setProcesso(processo);
        MinutaRecebidaService minutaRecebidaService = new MinutaRecebidaService(minutaService, minutasRecebidasRepository, processosRepository, minutaTarefaLogService);

        when(minutaService.save(minuta)).thenReturn(minuta);
        when(minutaService.save(novaMinuta)).thenReturn(novaMinuta);
        when(minutaService.salvarComVersao(novaMinuta, windson)).thenReturn(novaMinuta);
        when(minutaService.adicionaMovimento(any(Long.class), any(Long.class))).thenReturn(any());
        when(minutasRecebidasRepository.save(minutaRecebida)).thenReturn(minutaRecebida);
        MinutaRecebida minutaRecebida1 = minutaRecebidaService.aprovar(minutaRecebida, windson);
        assertEquals(MinutaRecebidaStatus.FINALIZADA, minutaRecebida1.getStatus());
        assertEquals(Minutar, minutaRecebida1.getProcesso().getTarefa());
    }

}
