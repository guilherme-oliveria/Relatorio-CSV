package br.jus.tjro.gabinete.scheduled.devolve.origem.builder;

import br.jus.tjro.gabinete.api.modelo.VariaveisInstancia;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.question.PersistenceEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VariaveisInstanciaBuilder {

    public static List<VariaveisInstancia> criaVariaveisDaRequisicao(Minuta minuta) {
        List<VariaveisInstancia> variaveis = new ArrayList<VariaveisInstancia>();

        if (minuta.getPublicacaoDje() != null)
            variaveis.add(new VariaveisInstancia("Viadiario", "true"));
        else
            variaveis.add(new VariaveisInstancia("Viadiario", "false"));

        if (minuta.getHaOutrasComunicacoes() != null && minuta.getHaOutrasComunicacoes())
            variaveis.add(new VariaveisInstancia("hacomunica", "true"));
        else
            variaveis.add(new VariaveisInstancia("hacomunica", "false"));

        if (minuta.getEmFaseFinalizacao() != null && minuta.getEmFaseFinalizacao())
            variaveis.add(new VariaveisInstancia("EmFaseFinalizacao", "true"));
        else
            variaveis.add(new VariaveisInstancia("EmFaseFinalizacao", "false"));

        if(minuta.temAlvara())
            variaveis.add(new VariaveisInstancia("alvaraEletronico", "true"));

        if(minuta.getTipoDocumento().isMinutaColegiado())
            variaveis.add(new VariaveisInstancia("tipoDecisaoGabinete", "colegiada"));
        else
            variaveis.add(new VariaveisInstancia("tipoDecisaoGabinete", "monocratica"));

        variaveis.addAll(
            minuta.getQuestions().stream()
                .filter(it->it.getPersistence() == PersistenceEnum.Fluxo && it.getValue() != null)
                .map(it-> new VariaveisInstancia(it.getKey(),it.getValue())).collect(Collectors.toList())
        );


        return variaveis;
    }

}
