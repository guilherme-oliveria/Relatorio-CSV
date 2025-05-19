package br.jus.tjro.gabinete.infrastructure.metrics;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;

public class ProcessoPorTarefa {
    private final TarefaEnum tarefa;
    private final Long orgaoJulgador;
    private final Long quantidade;

    public String[] tags() {
        return new String[] {"tarefa", tarefa.toString(), "orgao_julgador", orgaoJulgador.toString()};
    }

    public ProcessoPorTarefa(TarefaEnum tarefa, Long orgaoJulgador, Long quantidade) {
        this.tarefa = tarefa;
        this.orgaoJulgador = orgaoJulgador;
        this.quantidade = quantidade;
    }

    public Long getQuantidade() {
        return quantidade;
    }
}
