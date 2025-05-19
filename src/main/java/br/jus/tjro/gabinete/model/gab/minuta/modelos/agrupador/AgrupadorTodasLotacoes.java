package br.jus.tjro.gabinete.model.gab.minuta.modelos.agrupador;

import br.jus.tjro.gabinete.model.gab.minuta.modelos.BuscaModelo;

import java.util.HashMap;
import java.util.List;

class AgrupadorTodasLotacoes implements AgrupadorModelo {

    @Override
    public HashMap<String, List<String>> query(BuscaModelo modelo) {
        HashMap<String, List<String>> map = new Tj().query(modelo);
        map.putAll(modelo.getIdsLotacoes());
        map.put("label",List.of(AgrupadorModeloEnum.TODASVARAS.descricao));
        return map;
    }
}
