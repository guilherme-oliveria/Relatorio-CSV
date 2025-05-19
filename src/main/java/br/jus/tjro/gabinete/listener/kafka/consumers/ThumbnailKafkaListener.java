package br.jus.tjro.gabinete.listener.kafka.consumers;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.service.local.processo.documento.ThumbnailService;
import br.jus.tjro.gabinete.util.AuthenticationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

//@Component
//@Profile("!test")
//@ConditionalOnProperty(name="KAFKA_ENABLE_RUNNER", havingValue="true")
public class ThumbnailKafkaListener {

    private final ThumbnailService service;
    public static final String TOPIC_NAME =  "GABINETE_THUMBNAIL_PROC_DOC";
    private final Logger logger = LoggerFactory.getLogger(ThumbnailKafkaListener.class);

//    @Autowired
    public ThumbnailKafkaListener(ThumbnailService service) {
        this.service = service;
    }

//    @KafkaListener(topics = TOPIC_NAME)
    public void executa(ArrayList<Integer> idsProcessoDocumento) throws Exception {
        try {
            Usuario usuariLogado = AuthenticationUtil.setAuthenticationAdminInContext();
            idsProcessoDocumento.stream().forEach(id -> {
                try {
                    service.criaEhSalvaThumbnail(id.longValue());
                } catch (Exception e) {
                    logger.error("Erro ao gerar miniatura do documento id: "+id,e);
                }
            });
        } catch (Exception e) {
            throw new Exception("Erro ao processar mensagem.", e);
        }
    }
}
