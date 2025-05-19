package br.jus.tjro.gabinete.listener.model.processo.events;

import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;

public class PessoaUpdateEvent {

    private final Pessoa pessoa;

    public PessoaUpdateEvent(Pessoa entity) {
        this.pessoa = entity;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }
}
