package br.jus.tjro.gabinete.listener.kafka.consumers;

import br.jus.tjro.gabinete.listener.kafka.core.MessageListenerAbstract;
import br.jus.tjro.gabinete.listener.kafka.dtos.PedidoPautaDTO;
import br.jus.tjro.gabinete.listener.kafka.dtos.PreliminarDTO;
import br.jus.tjro.gabinete.listener.model.processo.listener.LocalizadorListener;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.question.QuestionBase;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.service.local.MinutaTarefaLogService;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import br.jus.tjro.gabinete.service.remoto.sg.PedidoInclusaoPautaRemotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static br.jus.tjro.gabinete.util.AuthenticationUtil.setAuthenticationAdminInContext;

@Component
public class IntegracaoPautaKafkaListener extends MessageListenerAbstract<Long> {

    private final ProcessosRepository processosRepository;
    private final MinutaTarefaLogService minutaTarefaLogService;
    private final PedidoInclusaoPautaRemotoService service;
    public static final String TOPIC_NAME =  "GABINETE_INTEGRACAO_PAUTA";
    private final Logger logger = LoggerFactory.getLogger(IntegracaoPautaKafkaListener.class);
    private final LocalizadorListener localizadorListener;

    @Autowired
    public IntegracaoPautaKafkaListener(PedidoInclusaoPautaRemotoService service,
                                        ProcessosRepository processosRepository,
                                        KafkaProducerService kafkaProducer,
                                        MinutaTarefaLogService minutaTarefaLogService, LocalizadorListener localizadorListener) {
        super(TOPIC_NAME, kafkaProducer, Long.class,false);
        this.service = service;
        this.processosRepository = processosRepository;
        this.minutaTarefaLogService = minutaTarefaLogService;
        this.localizadorListener = localizadorListener;
    }

    @Override
    public void executa(Long idProcesso) throws Exception {
        processosRepository.findById(idProcesso).ifPresent(processo -> {
            try {
                Usuario usuario = setAuthenticationAdminInContext();
                logger.info("Enviando pedido de inclusão de pauta. Numero Processo: " + processo.getNumeroProcesso());

                final Minuta minuta = service.enviaPauta(processo);
                if (minuta != null){
                    processo.naoConcluso();
                    minutaTarefaLogService.registrarLog(processo,usuario);
                    Processo processoUpdated = processosRepository.save(processo);
                    logger.info("Finalizando pedido de inclusão de pauta para o processo: " + processo.getNumeroProcesso());
                    localizadorListener.send(processoUpdated);

                    final String tipoVoto = minuta.getQuestions()
                        .stream()
                            .filter(m -> "tipoVoto".equals(m.getKey()))
                            .map(QuestionBase::getValue)
                            .findAny()
                        .orElseThrow(() -> new Exception("Tipo Voto não está presente na Minuta"));

                    final List<PreliminarDTO> preliminares = minuta.getPreliminares().stream()
                        .map(MinutaAnexo::getDTO)
                        .collect(Collectors.toList());

                    final PedidoPautaDTO pedidoPautaDTO = new PedidoPautaDTO(
                        processo.getIdProcessoSistemaLegado(),
                        processo.getNumeroProcesso(),
                        minuta.getId(),
                        processo.getOrgaoJulgador(), processo.getIdColegiado().orElse(null),
                        tipoVoto,
                        preliminares
                    );

                    kafkaProducer.send("SESSAO_PEDIDO_PAUTA", pedidoPautaDTO);
                } else {
                    logger.error("Erro ao processar pedido de inclusão de pauta. para o processo: " + processo.getNumeroProcesso());
                }
            } catch (Exception e) {
                logger.error("Erro ao processar mensagem. para id processo: " + idProcesso, e);
            }
        });
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
