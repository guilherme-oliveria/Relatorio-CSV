package br.jus.tjro.gabinete.scheduled.devolve.origem.tarefasdevolucoes;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.scheduled.devolve.origem.DevolveOrigemMinuta;
import br.jus.tjro.gabinete.scheduled.devolve.origem.DevolveOrigemMinutaAnexo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import static br.jus.tjro.gabinete.Tarefas.core.TarefaEnum.ParaIntegracao;
import static br.jus.tjro.gabinete.Tarefas.core.TarefaEnum.ParaIntegracaoComErro;
import static java.util.List.of;

@Component
public class ParaIntegracaoDevolucao implements TarefaDevolucao {

    private final Logger logger = LoggerFactory.getLogger(ParaIntegracaoDevolucao.class);
    private final DevolveOrigemMinuta devolveOrigemMinuta;
    private final DevolveOrigemMinutaAnexo devolveOrigemMinutaAnexo;
    private final ProcessosRepository processosRepository;

    public ParaIntegracaoDevolucao(DevolveOrigemMinuta devolveOrigemMinuta, DevolveOrigemMinutaAnexo devolveOrigemMinutaAnexo, ProcessosRepository processosRepository) {
        this.devolveOrigemMinuta = devolveOrigemMinuta;
        this.devolveOrigemMinutaAnexo = devolveOrigemMinutaAnexo;
        this.processosRepository = processosRepository;
    }

    @Override
    public List<TarefaEnum> getTarefas() {
        return of(ParaIntegracao, ParaIntegracaoComErro);
    }

    @Override
    public Processo devolverOrigem(Processo processo) throws Exception {
        try {
            List<MinutaAnexo> anexos = devolveOrigemMinuta.devolve(processo);
            processo = devolveOrigemMinutaAnexo.devolve(anexos,processo);
        } catch (Throwable e) {
            processo.tratarErroDevolucaoOrigem(e.getMessage());
            //processo.setErroIntegracao(e.getMessage());
            processo = processosRepository.save(processo);
            logger.error("Erro ao devolver minuta a origem! Enviando processo "+processo.getNumeroProcesso()+" para tarefa "+processo.getTarefa(), e);
            if(e.getMessage().contains("O servidor do pje diz"))
                throw e;
            else
                throw new TarefaDevolucaoException("Erro ao devolver minuta a origem! Enviando processo "+processo.getNumeroProcesso()+" para tarefa "+processo.getTarefa(), e);
        }
        logger.info("Processo integrado e movido no fluxo: " + processo.getNumeroProcesso());
        return processo;
    }

}
