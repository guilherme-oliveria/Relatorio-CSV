package br.jus.tjro.gabinete.Tarefas;

import br.jus.tjro.gabinete.Tarefas.core.TarefaAbstract;
import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.Tarefas.core.TarefaQualifier;
import org.springframework.stereotype.Component;

@Component
@TarefaQualifier(TarefaEnum.ParaIntegracaoSemManifestacao)
public class TarefaParaIntegracaoSemManifestacao extends TarefaAbstract {

    @Override
    public TarefaEnum getTarefa() {
        return TarefaEnum.ParaIntegracaoSemManifestacao;
    }

    @Override
    public TarefaEnum getProximaTarefa() {
        return TarefaEnum.NaoConcluso;
    }

    @Override
    public TarefaEnum getTarefaAnterior() {
        return TarefaEnum.Minutar;
    }

    @Override
    public boolean isTarefaVisivel() {
        return false;
    }

    @Override
    public String label() {
        return "Para integração sem manifestação";
    }

    @Override
    public String labelLote() {
        return "Para integração sem manifestação em lote";
    }

    @Override
    public boolean isTarefaEmLote() {
        return true;
    }
}
