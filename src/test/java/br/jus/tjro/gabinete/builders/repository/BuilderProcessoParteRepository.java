package br.jus.tjro.gabinete.builders.repository;

import br.jus.tjro.gabinete.model.gab.enums.TipoPolo;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte;
import br.jus.tjro.gabinete.model.gab.transiente.Partes;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoParteRepository;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BuilderProcessoParteRepository {

    public ProcessoParteRepository get() {
        ProcessoParteRepository repository = mock(ProcessoParteRepository.class);
        when(repository.findByProcesso(any())).thenAnswer(
            (Answer<List<ProcessoParte>>) invocation -> {
                Processo processo = invocation.getArgument(0);
                if (processo != null && processo.getId() == -1){
                    List<ProcessoParte> lista = new ArrayList<>();
                    lista.add(new ProcessoParte());
                    lista.add(new ProcessoParte());
                    lista.add(new ProcessoParte());
                    lista.add(new ProcessoParte());
                    lista.add(new ProcessoParte());
                    lista.add(new ProcessoParte());
                    return lista;
                }
                else {
                    return new ArrayList<>();
                }
            });

        List<Partes> partes = new ArrayList<>();
        Partes parte1 = new Partes();
        parte1.setId(1L);
        parte1.setNome("Parte 1");
        parte1.setTipoPolo(TipoPolo.A);
        partes.add(parte1);

        when(repository.buscaParteProcessoPorIdProcessoEPolo(eq(TipoPolo.A), any()))
            .thenReturn(partes);
        return repository;
    }
}
