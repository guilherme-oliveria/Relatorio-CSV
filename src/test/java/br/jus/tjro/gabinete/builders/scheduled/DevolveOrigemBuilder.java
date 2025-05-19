package br.jus.tjro.gabinete.builders.scheduled;

import br.jus.tjro.gabinete.listener.model.processo.listener.LocalizadorListener;
import br.jus.tjro.gabinete.scheduled.devolve.origem.DevolveOrigem;
import br.jus.tjro.gabinete.scheduled.devolve.origem.tarefasdevolucoes.TarefaDevolucao;
import br.jus.tjro.gabinete.service.local.MinutaMovimentoComplementoService;
import br.jus.tjro.gabinete.service.local.MinutaMovimentoService;
import br.jus.tjro.gabinete.service.local.MinutaService;
import br.jus.tjro.gabinete.service.local.ProcessoService;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class DevolveOrigemBuilder {

    public DevolveOrigemBuilder() { }

    public static DevolveOrigem get() throws Exception {
        Long servidorPublico = 0l;
        return new DevolveOrigem(servidorPublico,false, mock(LocalizadorListener.class), new ArrayList<>(),
            mock(ProcessoService.class), mock(MinutaService.class), mock(MinutaMovimentoService.class),
            mock(MinutaMovimentoComplementoService.class), mock(KafkaProducerService.class),null);
    }
}
