package br.jus.tjro.gabinete.Tarefas;

import br.jus.tjro.gabinete.Tarefas.core.TarefaAbstract;
import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.Tarefas.core.TarefaQualifier;
import org.springframework.stereotype.Component;

@Component
@TarefaQualifier(TarefaEnum.NaoConcluso)
public class TarefaNaoConcluso extends TarefaAbstract {

    @Override
    public TarefaEnum getTarefa() {
        return TarefaEnum.NaoConcluso;
    }

    @Override
    public TarefaEnum getProximaTarefa() {
        return TarefaEnum.Minutar;
    }

    @Override
    public TarefaEnum getTarefaAnterior() {
        return TarefaEnum.ParaIntegracao;
    }

    @Override
    public boolean isTarefaVisivel() {
        return false;
    }

    @Override
    public String label() {
        return "Não concluso";
    }

    @Override
    public String labelLote() {
        return "Não concluso";
    }

    @Override
    public boolean isTarefaEmLote() {
        return false;
    }
}
