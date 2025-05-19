package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.interfaces.Tarefa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TarefaService {

    private final List<Tarefa> tarefas;
    private List<TarefaEnum> tarefasEnumVisiveis;

    @Autowired
    public TarefaService(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    public List<TarefaEnum> getTarefaEnumVisiveis() {
        if (tarefasEnumVisiveis == null) {
            tarefasEnumVisiveis = new ArrayList<>();
            tarefas.forEach(t ->
            {
                if (t.isTarefaVisivel())
                    tarefasEnumVisiveis.add(t.getTarefa());
            });
        }
        return tarefasEnumVisiveis;
    }

}
