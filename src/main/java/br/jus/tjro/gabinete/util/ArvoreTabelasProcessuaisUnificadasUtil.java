package br.jus.tjro.gabinete.util;

import br.jus.tjro.gabinete.interfaces.Arvore;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public final class ArvoreTabelasProcessuaisUnificadasUtil {

    public static final <T extends Arvore> List<T> getArvore(List<T> lista) {

        List<T> listaRetorno = new LinkedList<>();

        long startTime = System.currentTimeMillis();

        for (T item : lista.stream().filter(x -> x.getIdPai() == null).collect(Collectors.toList())) {
            item.setFilhos(montaArvore(item, lista));
            listaRetorno.add(item);
        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        return listaRetorno;

    }

    private static final <T extends Arvore> List<T> montaArvore(T itemAtual, List<T> lista) {

        List<T> listTemp = new LinkedList<>();

        for (T item : lista.stream().filter(x -> x.getIdPai() != null && x.getIdPai().equals(itemAtual.getId()))
            .parallel().collect(Collectors.toList())) {
            lista.remove(item);
            item.setFilhos(montaArvore(item, lista));
            listTemp.add(item);

        }
        return listTemp;
    }

}
