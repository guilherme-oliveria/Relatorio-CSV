package br.jus.tjro.gabinete.config;

import br.jus.tjro.gabinete.service.websocket.WebSocketTransportHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@EnableScheduling
class WebsocketConfig implements WebSocketConfigurer {

    @Autowired
    private WebSocketTransportHandler transportadorHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(transportadorHandler, "/transportador").setAllowedOrigins("*");
    }


}
