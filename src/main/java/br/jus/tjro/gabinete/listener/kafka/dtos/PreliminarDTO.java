package br.jus.tjro.gabinete.listener.kafka.dtos;

import br.jus.tjro.gabinete.model.gab.enums.TipoVotoPreliminar;

public record PreliminarDTO(Long id, TipoVotoPreliminar tipoVoto, Integer ordem, Boolean prejudicaMerito) {
    public PreliminarDTO(Long id, TipoVotoPreliminar tipoVoto, Integer ordem, Boolean prejudicaMerito) {
        this.id = id;
        this.tipoVoto = tipoVoto;
        this.ordem = ordem;
        this.prejudicaMerito = prejudicaMerito;
    }
}
