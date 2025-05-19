package br.jus.tjro.gabinete.service.websocket;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.repository.WebSocketSessionRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class WebSocketTransportHandler extends TextWebSocketHandler implements WebSocketSessionRepository {

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public List<WebSocketSession> getSessions(){
        return Collections.unmodifiableList(this.sessions);
    }

    @Override
    public List<WebSocketSession> getSessionsBy(Usuario usuario){
        return this.sessions.stream()
            .filter(s ->
                ((UsernamePasswordAuthenticationToken) s.getPrincipal())
                    .getCredentials().equals(usuario))
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connection established");
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
        throws Exception {
        if ("CLOSE".equalsIgnoreCase(message.getPayload())) {
            session.close();
            sessions.remove(session);
        } else {
            System.out.println("Received:" + message.getPayload());
        }
    }
}
