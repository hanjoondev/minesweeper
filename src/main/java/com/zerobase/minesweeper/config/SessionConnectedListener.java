package com.zerobase.minesweeper.config;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;

@Component
public class SessionConnectedListener implements ApplicationListener<SessionConnectedEvent> {

    private final SubProtocolWebSocketHandler subProtocolWebSocketHandler;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    public SessionConnectedListener(WebSocketHandler webSocketHandler, SimpMessageSendingOperations simpMessageSendingOperations) {
        this.subProtocolWebSocketHandler = (SubProtocolWebSocketHandler) webSocketHandler;
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        simpMessageSendingOperations.convertAndSend("/topic/lobbyUsers",
                subProtocolWebSocketHandler.getStats().getWebSocketSessions());
    }
}
