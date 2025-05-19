package br.jus.tjro.gabinete.repository;

import br.jus.tjro.gabinete.model.gab.Usuario;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public interface WebSocketSessionRepository {

    List<WebSocketSession> getSessions();
    List<WebSocketSession> getSessionsBy(Usuario usuario);
}
