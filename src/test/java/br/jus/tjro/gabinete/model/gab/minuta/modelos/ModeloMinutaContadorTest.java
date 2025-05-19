package br.jus.tjro.gabinete.model.gab.minuta.modelos;

import br.jus.tjro.gabinete.UnitTest;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static br.jus.tjro.gabinete.model.gab.minuta.modelos.agrupador.AgrupadorModeloEnum.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModeloMinutaContadorTest {

    @Test
    public void testConstrutorVazio(){
        ModeloMinutaContador contador = new ModeloMinutaContador();
        assertEquals(0,((long) contador.getMeus()));
        assertEquals(0,((long) contador.getTj()));
        assertEquals(0,((long) contador.getTodasVaras()));
        assertEquals(0,((long) contador.getVara()));
    }

    @Test
    public void contrutorHashMap(){
        Map<String, Long> map = Map.of(MEUS.descricao,1l,VARA.descricao,4l,TODASVARAS.descricao,3l,TJ.descricao,2l);
        ModeloMinutaContador contador = new ModeloMinutaContador(new HashMap<>(map));
        assertEquals(1,((long) contador.getMeus()));
        assertEquals(2,((long) contador.getTj()));
        assertEquals(3,((long) contador.getTodasVaras()));
        assertEquals(4,((long) contador.getVara()));
    }
}
