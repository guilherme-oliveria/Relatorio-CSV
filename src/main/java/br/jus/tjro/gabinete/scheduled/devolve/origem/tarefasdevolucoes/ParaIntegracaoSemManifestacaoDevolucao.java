package br.jus.tjro.gabinete.scheduled.devolve.origem.tarefasdevolucoes;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.scheduled.devolve.origem.DevolveOrigemSemManifestacao;
import br.jus.tjro.gabinete.util.AuthenticationUtil;
import org.springframework.stereotype.Component;

import java.util.List;

import static br.jus.tjro.gabinete.Tarefas.core.TarefaEnum.ParaIntegracaoSemManifestacao;

@Component
public class ParaIntegracaoSemManifestacaoDevolucao implements TarefaDevolucao {

    private final DevolveOrigemSemManifestacao devolveSemManifestacao;

    public ParaIntegracaoSemManifestacaoDevolucao(DevolveOrigemSemManifestacao devolveSemManifestacao) {
        this.devolveSemManifestacao = devolveSemManifestacao;
    }

    @Override
    public List<TarefaEnum> getTarefas() {
        return List.of(ParaIntegracaoSemManifestacao);
    }

    @Override
    public Processo devolverOrigem(Processo processo) throws Exception {
        Usuario usuario = AuthenticationUtil.setAuthenticationAdminInContext();
        return devolveSemManifestacao.devolve(processo, usuario);
    }
}
