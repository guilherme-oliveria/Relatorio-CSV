package br.jus.tjro.gabinete.Tarefas.core;

import br.jus.tjro.gabinete.interfaces.Tarefa;

public abstract class TarefaAbstract implements Tarefa {

    @Override
    public String toString() {
        return getTarefa().getTarefa();
    }

}
