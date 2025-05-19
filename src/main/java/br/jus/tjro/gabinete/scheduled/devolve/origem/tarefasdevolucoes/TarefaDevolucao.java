package br.jus.tjro.gabinete.scheduled.devolve.origem.tarefasdevolucoes;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.model.gab.processo.Processo;

import java.util.List;

public interface TarefaDevolucao {
    List<TarefaEnum> getTarefas();
    Processo devolverOrigem(Processo processo) throws Exception;
}
