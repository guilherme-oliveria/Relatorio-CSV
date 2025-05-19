package br.jus.tjro.gabinete.model.gab.minuta.modelos.agrupador;

import br.jus.tjro.gabinete.model.gab.minuta.modelos.BuscaModelo;

import java.util.HashMap;
import java.util.List;

interface AgrupadorModelo {


    HashMap<String, List<String>> query(BuscaModelo modelo);
}
