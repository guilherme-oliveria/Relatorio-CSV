package br.jus.tjro.gabinete.Tarefas;

import br.jus.tjro.gabinete.Tarefas.core.TarefaAbstract;
import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.Tarefas.core.TarefaQualifier;
import org.springframework.stereotype.Component;

@Component
@TarefaQualifier(TarefaEnum.Minutar)
public class TarefaMinutar extends TarefaAbstract {

    @Override
    public TarefaEnum getTarefa() {
        return TarefaEnum.Minutar;
    }

    @Override
    public TarefaEnum getProximaTarefa() {
        return TarefaEnum.Assinar;
    }

    @Override
    public TarefaEnum getTarefaAnterior() {
        return null;
    }

    @Override
    public boolean isTarefaVisivel() {
        return true;
    }

    @Override
    public String label() {
        return "Label Minutar";
    }

    @Override
    public String labelLote() {
        return "Label Minutar Lote";
    }

    @Override
    public boolean isTarefaEmLote() {
        return true;
    }
}
