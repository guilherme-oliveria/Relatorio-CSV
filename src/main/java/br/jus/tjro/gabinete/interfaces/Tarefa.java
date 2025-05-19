package br.jus.tjro.gabinete.interfaces;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;

public interface Tarefa {

    TarefaEnum getTarefa();

    TarefaEnum getProximaTarefa();

    TarefaEnum getTarefaAnterior();

    boolean isTarefaVisivel();

    String label();

    String labelLote();

    boolean isTarefaEmLote();

    @Override
    String toString();

}
