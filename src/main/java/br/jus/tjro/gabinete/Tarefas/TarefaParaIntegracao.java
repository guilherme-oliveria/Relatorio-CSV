package br.jus.tjro.gabinete.Tarefas;

import br.jus.tjro.gabinete.Tarefas.core.TarefaAbstract;
import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.Tarefas.core.TarefaQualifier;
import org.springframework.stereotype.Component;

@Component
@TarefaQualifier(TarefaEnum.ParaIntegracao)
public class TarefaParaIntegracao extends TarefaAbstract {

    @Override
    public TarefaEnum getTarefa() {
        return TarefaEnum.ParaIntegracao;
    }

    @Override
    public TarefaEnum getProximaTarefa() {
        return TarefaEnum.NaoConcluso;
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
        return "Para integração";
    }

    @Override
    public String labelLote() {
        return "Para integração em lote";
    }

    @Override
    public boolean isTarefaEmLote() {
        return true;
    }
}
