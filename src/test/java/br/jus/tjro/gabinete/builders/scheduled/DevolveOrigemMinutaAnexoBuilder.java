package br.jus.tjro.gabinete.builders.scheduled;

import br.jus.tjro.gabinete.builders.repository.BuilderProcessoRepository;
import br.jus.tjro.gabinete.builders.service.*;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.scheduled.devolve.origem.DevolveOrigem;
import br.jus.tjro.gabinete.scheduled.devolve.origem.DevolveOrigemMinuta;
import br.jus.tjro.gabinete.scheduled.devolve.origem.DevolveOrigemMinutaAnexo;
import br.jus.tjro.gabinete.scheduled.devolve.origem.DevolveOrigemSemManifestacao;
import br.jus.tjro.gabinete.service.assinaturadigital.RetornoAssinaturaService;
import br.jus.tjro.gabinete.service.local.*;
import br.jus.tjro.gabinete.service.local.verifica_integracao.VerificaIntegracaoService;
import br.jus.tjro.gabinete.service.remoto.DocumentoRemotoService;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import br.jus.tjro.gabinete.service.remoto.processo.ProcessoRemotoService;

import static org.mockito.Mockito.mock;

public class DevolveOrigemMinutaAnexoBuilder {


    public DevolveOrigemMinutaAnexoBuilder() { }

    public static DevolveOrigemMinutaAnexo get() throws Exception {
        ProcessosRepository processoRepository = new BuilderProcessoRepository().get();
        MinutaAnexoService documentoBinService = new BuilderMinutaAnexoServiceService().get();
        ProcessoRemotoService processoRemotoService = new BuilderProcessoRemotoService().get();
        RetornoAssinaturaService retornoAssinatura = BuilderRetornoAssinaturaService.get();
        MinutaMovimentoService movimentoService = null;
        KafkaProducerService kafkaService = null;
        MinutaTarefaLogService tarefaService = mock(MinutaTarefaLogService.class);
        MinutaMovimentoComplementoService complementoService = null;
        return new DevolveOrigemMinutaAnexo(retornoAssinatura,movimentoService,processoRemotoService,
            complementoService,documentoBinService, tarefaService, kafkaService,
            processoRepository);
    }
}
