package br.jus.tjro.gabinete.model.gab.minuta.modelos.agrupador;

import br.jus.tjro.gabinete.model.gab.minuta.modelos.BuscaModelo;

import java.util.HashMap;
import java.util.List;

public class Tj implements AgrupadorModelo {

    @Override
    public HashMap<String, List<String>> query(BuscaModelo modelo) {
        HashMap<String, List<String>> busca = modelo.getBusca();
        HashMap<String, List<String>> tipoDocumento = modelo.getIdTipoDocumento();
        HashMap<String, List<String>> retorno = new HashMap<>();
        if(busca != null)
            retorno.putAll(busca);
        if(tipoDocumento != null)
            retorno.putAll(tipoDocumento);
        retorno.put("sistema",List.of("gabinete"));
        retorno.put("label",List.of(AgrupadorModeloEnum.TJ.descricao));
        retorno.put("tags",modelo.getTags());
        return retorno;
    }
}
