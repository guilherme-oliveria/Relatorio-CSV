package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.model.gab.Papel;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.repository.webjud.OrgaosJulgadoresRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class RevisorServiceTest {

    private final ProcessoService processoService = mock(ProcessoService.class);

    private final OrgaosJulgadoresRepository orgaosJulgadoresRepository = mock(OrgaosJulgadoresRepository.class);
    private final RevisorService revisorService = new RevisorService(
        orgaosJulgadoresRepository,mock(MinutaService.class), mock(ProcessosRepository.class),processoService);

    private Processo getProcessoComEstado() throws Exception {
        Processo p = new Processo();
        p.setTarefa(TarefaEnum.Minutar);
        return p;
    }

    @Test()
    public void deveMudarTarefaProcessoParaRevisar() throws Exception {
        Processo processo = this.getProcessoComEstado();
        OrgaoJulgador oj = new OrgaoJulgador("PJESG-1");
        OrgaoJulgador ojRevisor = new OrgaoJulgador("PJESG-2");
        processo.setOrgaoJulgadorObj(oj);
        Usuario usuario = new Usuario("Tanjiro", "1", List.of(), "token");
        Papel papel = new Papel("Administrador", List.of(oj));
        Map<String, List<Papel>> mapPapeis = new HashMap<String, List<Papel>>();
        mapPapeis.put("PJEPG", List.of(papel));
        usuario.setPapeis(mapPapeis);

        when(processoService.findById(anyLong())).thenReturn(Optional.of(processo));
        when(orgaosJulgadoresRepository.findSouRevisadoPor(oj)).thenReturn(List.of(ojRevisor));
        revisorService.enviaProcessoParaRevisor(anyLong(), usuario);
        assertEquals(processo.getTarefa(),TarefaEnum.Revisar);
    }
}

