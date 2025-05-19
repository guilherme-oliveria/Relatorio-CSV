package br.jus.tjro.gabinete.model.gab.minuta.modelos.agrupador;

import br.jus.tjro.gabinete.model.gab.minuta.modelos.BuscaModelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgrupadorModeloManager {

    AgrupadorModelo agrupadorMeu = new Meu();
    AgrupadorModelo agrupadorTj = new Tj();
    AgrupadorModelo agrupadorVara = new AgrupadorLotacaoAtual();
    AgrupadorModelo agrupadorTodasAsVaras = new AgrupadorTodasLotacoes();

    public HashMap<String, List<String>> buscaQuery(BuscaModelo buscaModelo, AgrupadorModeloEnum agrupadorEnum){
        switch (agrupadorEnum){
            case MEUS:
                return agrupadorMeu.query(buscaModelo);
            case VARA:
                return agrupadorVara.query(buscaModelo);
            case TODASVARAS:
                return agrupadorTodasAsVaras.query(buscaModelo);
            case TJ:
                return agrupadorTj.query(buscaModelo);
            default:
                return new HashMap<>(Map.of("label",List.of("default")));
        }
    }
}
