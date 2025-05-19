package br.jus.tjro.gabinete.Tarefas;

import br.jus.tjro.gabinete.Tarefas.core.TarefaAbstract;
import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.Tarefas.core.TarefaQualifier;
import org.springframework.stereotype.Component;

@Component
@TarefaQualifier(TarefaEnum.Assinar)
public class TarefaAssinar extends TarefaAbstract {

    @Override
    public TarefaEnum getTarefa() {
        return TarefaEnum.Assinar;
    }

    @Override
    public TarefaEnum getProximaTarefa() {
        return TarefaEnum.Assinando;
    }

    @Override
    public TarefaEnum getTarefaAnterior() {
        return TarefaEnum.Minutar;
    }

    @Override
    public boolean isTarefaVisivel() {
        return true;
    }

    @Override
    public String label() {
        return "Label Assinar";
    }

    @Override
    public String labelLote() {
        return "Label Assinar Lote";
    }

    @Override
    public boolean isTarefaEmLote() {
        return true;
    }
}
