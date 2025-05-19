package br.jus.tjro.gabinete.importacao.builder;

import br.jus.tjro.gabinete.listener.model.processo.listener.LocalizadorListener;
import br.jus.tjro.gabinete.scheduled.TodosScheduled;

import static org.mockito.Mockito.mock;

public class BuilderTodosScheduled {

    public TodosScheduled get() throws Exception {
        BuilderBuscaPartesDosProcessos builderPartesProcesso = new BuilderBuscaPartesDosProcessos();
        BuilderBuscaEnderecosDasPartesDosProcessos builderBuscaEnderecosDasPartesDosProcessos = new BuilderBuscaEnderecosDasPartesDosProcessos();
        TodosScheduled scheduled = new TodosScheduled(true,
            builderPartesProcesso.get(),
            builderBuscaEnderecosDasPartesDosProcessos.get(), mock(LocalizadorListener.class));
        return scheduled;
    }


}
