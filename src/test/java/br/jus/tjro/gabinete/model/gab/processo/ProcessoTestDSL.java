package br.jus.tjro.gabinete.model.gab.processo;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.UnitTest;
import io.vavr.collection.List;
import org.junit.experimental.categories.Category;

import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ProcessoTestDSL {
    private final TarefaEnum[] tarefas;
    private final Function<TarefaEnum, Processo> mapper;
    private final Class<? extends Throwable> exceptionClass;

    ProcessoTestDSL(TarefaEnum... tarefas) {
        this(tarefas, null, null);
    }

    private ProcessoTestDSL(TarefaEnum[] tarefas, Function<TarefaEnum, Processo> mapper, Class<? extends Throwable> exceptionClass) {
        this.tarefas = tarefas;
        this.mapper = mapper;
        this.exceptionClass = exceptionClass;
    }

    ProcessoTestDSL dadoQue(Function<TarefaEnum, Processo> mapper) {
        return new ProcessoTestDSL(tarefas, mapper, null);
    }

    ProcessoTestDSL deveLancar(Class<? extends Throwable> exceptionClass) {
        return new ProcessoTestDSL(this.tarefas, this.mapper, exceptionClass);
    }

    void quando(Consumer<Processo> consumer) {
        List.of(this.tarefas).
            map(this.mapper).
            forEach(p -> assertThrows(exceptionClass, () -> consumer.accept(p)));
    }

}
