package br.jus.tjro.gabinete.service.websocket;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.repository.WebSocketSessionRepository;
import br.jus.tjro.gabinete.model.gab.transiente.Notificacao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

@Service
public class WebSocketTransportService implements WebSocketTransport {

    private final WebSocketSessionRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketTransportService.class);

    @Autowired
    WebSocketTransportService(WebSocketSessionRepository repository){
        this.repository = repository;
    }

    @Override
    public void send(Usuario usuario, Notificacao notificacao) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TextMessage texto = new TextMessage(mapper.writeValueAsString(notificacao));
        repository.getSessionsBy(usuario).stream().forEach(p -> {
            try {
                p.sendMessage(texto);
            } catch (IOException e) {
                logger.warn("Não foi possivel entregar a mensagem",e);
            }
        });
    }
}
