package br.jus.tjro.gabinete.model.gab.transiente;

import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaMovimento;
import br.jus.tjro.gabinete.model.gab.tpu.TpuMovimento;

import java.util.List;
import java.util.Map;

public class MinutaEmLote {
    private Minuta minuta;
    private Map<Long, List<MinutaMovimento>> movimentos;

    public MinutaEmLote() {}

    public MinutaEmLote(Minuta minuta, Map<Long, List<MinutaMovimento>> minutaMovmentos) {
        this.minuta = minuta;
        this.movimentos = minutaMovmentos;
    }

    public Minuta getMinuta() {
        return minuta;
    }

    public void setMinuta(Minuta minuta) {
        this.minuta = minuta;
    }

    public Map<Long, List<MinutaMovimento>> getMovimentos() {
        return movimentos;
    }

    public void setMovimentos(Map<Long, List<MinutaMovimento>> movimentos) {
        this.movimentos = movimentos;
    }
}
