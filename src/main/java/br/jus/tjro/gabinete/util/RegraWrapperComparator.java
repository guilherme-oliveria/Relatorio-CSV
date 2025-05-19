package br.jus.tjro.gabinete.util;

import br.jus.tjro.gabinete.model.gab.localizador.RegraWrapper;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class RegraWrapperComparator implements Comparator {


    @Override
    public int compare(Object o1, Object o2) {
        RegraWrapper regra = (RegraWrapper) o1;
        RegraWrapper regra2 = (RegraWrapper) o2;
        return regra.getValor().compareTo(regra2.getValor());
    }

    @Override
    public Comparator reversed() {
        return null;
    }

    @Override
    public Comparator thenComparing(Comparator other) {
        return null;
    }

    @Override
    public Comparator thenComparingInt(ToIntFunction keyExtractor) {
        return null;
    }

    @Override
    public Comparator thenComparingLong(ToLongFunction keyExtractor) {
        return null;
    }

    @Override
    public Comparator thenComparingDouble(ToDoubleFunction keyExtractor) {
        return null;
    }

    @Override
    public Comparator thenComparing(Function keyExtractor) {
        return null;
    }

    @Override
    public Comparator thenComparing(Function keyExtractor, Comparator keyComparator) {
        return null;
    }
}
