package br.jus.tjro.gabinete.service.local.verifica_integracao;

import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.service.remoto.processo.ProcessoRemotoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VerificaIntegracaoService {


    private final ProcessoRemotoService processoRemotoService;
    private final List<String> tarefas;
    private final Boolean checaTarefaConcluso;

    VerificaIntegracaoService(ProcessoRemotoService processoRemotoService,
                              @Value("${verifica.tarefas.concluso:true}") Boolean checaTarefaConcluso,
                              @Value("${tarefas.concluso:Concluso - Gabinete}") List<String> tarefas){
        this.processoRemotoService = processoRemotoService;
        this.tarefas = tarefas;
        this.checaTarefaConcluso = checaTarefaConcluso;
    }


    public List<String> buscaTarefas(Processo processo) throws ServicoRemotoException {
        return processoRemotoService.buscaTarefaAtual(processo);
    }

    public boolean processoIsConclusoGabinete(Processo processo) throws ServicoRemotoException {
        if(this.checaTarefaConcluso) {
            List<String> tarefasAtual = buscaTarefas(processo);
            return tarefas.stream().anyMatch(it -> tarefasAtual.stream().anyMatch(atual -> atual.contains(it)));
        }
        return true;
    }
}
