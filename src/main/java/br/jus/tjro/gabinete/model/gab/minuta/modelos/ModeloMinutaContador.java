package br.jus.tjro.gabinete.model.gab.minuta.modelos;

import br.jus.tjro.gabinete.model.gab.minuta.modelos.agrupador.AgrupadorModeloEnum;

import java.util.HashMap;
import java.util.Map;

import static br.jus.tjro.gabinete.model.gab.minuta.modelos.agrupador.AgrupadorModeloEnum.*;

public class ModeloMinutaContador {

    private final Long meus;
    private final Long vara;
    private final Long todasVaras;
    private final Long tj;


    public ModeloMinutaContador(Map<String, Long> totais) {
        this(new HashMap(totais));
    }

    public ModeloMinutaContador(HashMap<String, Long> totais) {
        this(totais.get(MEUS.descricao),totais.get(VARA.descricao),totais.get(TODASVARAS.descricao),totais.get(TJ.descricao));
    }

    public ModeloMinutaContador() {
        this(0l,0l,0l,0l);
    }

    public ModeloMinutaContador(Long meus, Long vara, Long todasVaras, Long tj) {
        this.meus = meus;
        this.vara = vara;
        this.todasVaras = todasVaras;
        this.tj = tj;
    }

    public Long getMeus() {
        return meus;
    }


    public Long getVara() {
        return vara;
    }


    public Long getTj() {
        return tj;
    }


    public Long getTodasVaras() {
        return todasVaras;
    }

}
