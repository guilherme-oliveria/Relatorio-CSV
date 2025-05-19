package br.jus.tjro.gabinete.model.gab.minuta.modelos.agrupador;

import br.jus.tjro.gabinete.model.gab.minuta.modelos.BuscaModelo;

import java.util.HashMap;
import java.util.List;

class AgrupadorLotacaoAtual implements AgrupadorModelo {

    @Override
    public HashMap<String, List<String>> query(BuscaModelo modelo) {
        HashMap<String, List<String>> map = new Tj().query(modelo);
        map.putAll(modelo.getLotacaoAtual());
        map.put("label",List.of(AgrupadorModeloEnum.VARA.descricao));
        return map;
    }
}
