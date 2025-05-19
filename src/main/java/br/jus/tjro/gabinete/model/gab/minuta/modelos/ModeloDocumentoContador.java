package br.jus.tjro.gabinete.model.gab.minuta.modelos;

import br.jus.tjro.gabinete.model.gab.minuta.modelos.agrupador.AgrupadorModeloEnum;

import java.util.HashMap;
import java.util.Map;

import static br.jus.tjro.gabinete.model.gab.minuta.modelos.agrupador.AgrupadorModeloEnum.*;

public class ModeloDocumentoContador {

    private final String chave;
    private final String descricao;
    private final Long quantidade;

    public ModeloDocumentoContador(String chave, String descricao, Long quantidade) {
        this.chave = chave;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }

    public ModeloDocumentoContador(AgrupadorModeloEnum agrupador, long quantidade) {
        this(agrupador.name(),agrupador.descricao,quantidade);
    }

    public String getChave() {
        return chave;
    }

    public String getDescricao() {
        return descricao;
    }

    public Long getQuantidade() {
        return quantidade;
    }
}
