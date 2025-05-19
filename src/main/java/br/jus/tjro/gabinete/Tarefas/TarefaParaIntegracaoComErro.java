package br.jus.tjro.gabinete.Tarefas;

import br.jus.tjro.gabinete.Tarefas.core.TarefaAbstract;
import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.Tarefas.core.TarefaQualifier;
import org.springframework.stereotype.Component;

@Component
@TarefaQualifier(TarefaEnum.ParaIntegracaoComErro)
public class TarefaParaIntegracaoComErro extends TarefaAbstract {

    @Override
    public TarefaEnum getTarefa() {
        return TarefaEnum.ParaIntegracaoComErro;
    }

    @Override
    public TarefaEnum getProximaTarefa() {
        return TarefaEnum.ParaIntegracao;
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
        return "Erro na Integração";
    }

    @Override
    public String labelLote() {
        return "";
    }

    @Override
    public boolean isTarefaEmLote() {
        return false;
    }
}
