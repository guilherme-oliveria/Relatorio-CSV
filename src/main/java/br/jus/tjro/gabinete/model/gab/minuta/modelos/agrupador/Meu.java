package br.jus.tjro.gabinete.model.gab.minuta.modelos.agrupador;

import br.jus.tjro.gabinete.model.gab.minuta.modelos.BuscaModelo;

import java.util.HashMap;
import java.util.List;

class Meu implements AgrupadorModelo {

    @Override
    public HashMap<String, List<String>> query(BuscaModelo modelo) {
        HashMap<String, List<String>> map = new Tj().query(modelo);
        map.put("label",List.of(AgrupadorModeloEnum.MEUS.descricao));
        map.putAll(modelo.getCpf());

        return map;
    }
}
