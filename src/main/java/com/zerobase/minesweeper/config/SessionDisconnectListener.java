package com.zerobase.minesweeper.config;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;

@Component
public class SessionDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {

    private final SubProtocolWebSocketHandler subProtocolWebSocketHandler;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    public SessionDisconnectListener(WebSocketHandler webSocketHandler, SimpMessageSendingOperations simpMessageSendingOperations) {
        this.subProtocolWebSocketHandler = (SubProtocolWebSocketHandler) webSocketHandler;
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        simpMessageSendingOperations.convertAndSend("/topic/lobbyUsers",
                subProtocolWebSocketHandler.getStats().getWebSocketSessions());
    }
}
