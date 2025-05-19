package br.jus.tjro.gabinete.service.websocket;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.transiente.Notificacao;

import java.io.IOException;

public interface WebSocketTransport {

    void send(Usuario usuario, Notificacao notificacao) throws IOException;
}
