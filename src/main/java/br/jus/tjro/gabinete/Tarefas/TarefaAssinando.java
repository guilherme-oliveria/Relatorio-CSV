package br.jus.tjro.gabinete.Tarefas;

import br.jus.tjro.gabinete.Tarefas.core.TarefaAbstract;
import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.Tarefas.core.TarefaQualifier;
import org.springframework.stereotype.Component;

@Component
@TarefaQualifier(TarefaEnum.Assinando)
public class TarefaAssinando extends TarefaAbstract {

    @Override
    public TarefaEnum getTarefa() {
        return TarefaEnum.Assinando;
    }

    @Override
    public TarefaEnum getProximaTarefa() {
        return TarefaEnum.ParaIntegracao;
    }

    @Override
    public TarefaEnum getTarefaAnterior() {
        return TarefaEnum.Assinar;
    }

    @Override
    public boolean isTarefaVisivel() {
        return false;
    }

    @Override
    public String label() {
        return "Assinando";
    }

    @Override
    public String labelLote() {
        return "Assinando em Lote";
    }

    @Override
    public boolean isTarefaEmLote() {
        return true;
    }
}
