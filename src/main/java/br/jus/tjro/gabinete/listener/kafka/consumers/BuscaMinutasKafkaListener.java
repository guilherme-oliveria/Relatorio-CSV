package br.jus.tjro.gabinete.listener.kafka.consumers;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.listener.model.processo.listener.LocalizadorListener;
import br.jus.tjro.gabinete.model.gab.enums.SistemaOrigemEnum;
import br.jus.tjro.gabinete.model.gab.localizador.Caixa;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaRecebida;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoTag;
import br.jus.tjro.gabinete.repository.gab.minuta.MinutasRecebidasRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.scheduled.BuscaProcessosConclusos;
import br.jus.tjro.gabinete.service.local.CaixaService;
import br.jus.tjro.gabinete.service.local.TagService;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import br.jus.tjro.gabinete.util.AuthenticationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Optional;

@Configuration
@Profile("!test")
@ConditionalOnProperty(name="KAFKA_ENABLE_RUNNER", havingValue="true")
public class BuscaMinutasKafkaListener {

    public static final String TOPIC_NAME =  "GABINETE_MINUTAS_RECEBIDAS";
    private static final String TOPIC_MINUTAS_RECEBIDAS_RETORNO = TOPIC_NAME + "_RETORNO";
    private final Logger logger = LoggerFactory.getLogger(br.jus.tjro.gabinete.listener.kafka.consumers.BuscaMinutasKafkaListener.class);
    private final BuscaProcessosConclusos buscaProcesso;
    private final ProcessosRepository processosRepository;
    private final KafkaProducerService kafkaProducerService;
    private final MinutasRecebidasRepository minutasRecebidasRepository;
    private final TagService tagService;
    private final CaixaService caixaService;
    private final LocalizadorListener localizadorListener;

    @Autowired
    public BuscaMinutasKafkaListener(BuscaProcessosConclusos buscaProcesso,
                                     ProcessosRepository processosRepository,
                                     KafkaProducerService kafkaProducerService,
                                     MinutasRecebidasRepository minutasRecebidasRepository,
                                     CaixaService caixaService,
                                     TagService tagService, LocalizadorListener localizadorListener) {
        this.buscaProcesso = buscaProcesso;
        this.processosRepository = processosRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.minutasRecebidasRepository = minutasRecebidasRepository;
        this.tagService = tagService;
        this.caixaService = caixaService;
        this.localizadorListener = localizadorListener;
    }

    @KafkaListener(topics = TOPIC_NAME, properties = "spring.json.value.default.type=br.jus.tjro.gabinete.model.gab.minuta.MinutaRecebida")
    public void executa(MinutaRecebida recebida) throws Exception {
        try {
            var processo = processosRepository
                .findByIdProcessoSistemaLegadoAndSistema(recebida.getIdProcessoSistemaLegado(),recebida.getSistema())
                .orElseGet(() -> {
                    try {
                        return this.buscaProcesso.buscaProcessosNoWebService(recebida.getSistema(),
                            recebida.getIdProcessoSistemaLegado(),
                            TarefaEnum.NaoConcluso,
                        AuthenticationUtil.setAuthenticationAdminInContext());
                    } catch (Exception e) {
                        throw new RuntimeException("Erro ao recuperar o processo com id "
                            +recebida.getIdProcessoSistemaLegado()+
                            " e sistema "+recebida.getSistema(),e);
                    }
            });
            Optional<ProcessoTag> tagValidarExpediente = this.tagService.getTagValidarExpediente(processo);
            Caixa caixaValidarExpediente = this.caixaService.getCaixaValidarExpedienteCartorio();

            recebida.setProcesso(processo, tagValidarExpediente, caixaValidarExpediente);
            Processo processoUpdated = processosRepository.save(processo);
            localizadorListener.send(processoUpdated);
            minutasRecebidasRepository.save(recebida);
            kafkaProducerService.send(getTopicoRetorno(recebida.getSistemaOrigem()), recebida.getResposta());
        } catch (Exception e) {
            recebida.comErro();
            kafkaProducerService.send(getTopicoRetorno(recebida.getSistemaOrigem()), recebida.getResposta());
            throw new Exception("Erro ao processar mensagem de minutaRecebida. com processo id "+recebida.getIdProcessoSistemaLegado()+
                " e sistema "+recebida.getSistema(), e);
        }
    }

    String getTopicoRetorno(SistemaOrigemEnum sistemaOrigemEnum) {
        return TOPIC_MINUTAS_RECEBIDAS_RETORNO + "_" + sistemaOrigemEnum.getDescricao().toUpperCase();
    }
}
