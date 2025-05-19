package br.jus.tjro.gabinete.listener.model.processo.events;

import br.jus.tjro.gabinete.model.gab.processo.ProcessoTag;

public class ProcessoTagEvent {

    private final ProcessoTag processoTag;

    public ProcessoTagEvent(ProcessoTag entity) {
        this.processoTag = entity;
    }

    public ProcessoTag getProcessoTag() {
        return processoTag;
    }
}
