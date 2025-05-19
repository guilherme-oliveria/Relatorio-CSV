package br.jus.tjro.gabinete.Tarefas;

import br.jus.tjro.gabinete.Tarefas.core.TarefaAbstract;
import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.Tarefas.core.TarefaQualifier;
import org.springframework.stereotype.Component;

@Component
@TarefaQualifier(TarefaEnum.Corrigir)
public class TarefaCorrigir extends TarefaAbstract {

    @Override
    public TarefaEnum getTarefa() {
        return TarefaEnum.Corrigir;
    }

    @Override
    public TarefaEnum getProximaTarefa() {
        return TarefaEnum.Minutar;
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
        return "Label Corrigir";
    }

    @Override
    public String labelLote() {
        return "Label Corrigir Lote";
    }

    @Override
    public boolean isTarefaEmLote() {
        return false;
    }
}
