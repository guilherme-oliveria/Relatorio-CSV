package br.jus.tjro.gabinete.listener.model.processo.events;

import br.jus.tjro.gabinete.model.gab.processo.Processo;

public class ProcessoCreateOrUpdateEvent {

    private final Processo processo;

    public ProcessoCreateOrUpdateEvent(Processo processo) {
        this.processo = processo;
    }

    public Processo getProcesso() {
        return this.processo;
    }

}
