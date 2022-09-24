package com.zerobase.minesweeper.config;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler.*;

@Component
public class SessionDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {

    private final Stats websocketSessionStats;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    public SessionDisconnectListener(WebSocketHandler webSocketHandler, SimpMessageSendingOperations simpMessageSendingOperations) {
        SubProtocolWebSocketHandler subProtocolWebSocketHandler = (SubProtocolWebSocketHandler) webSocketHandler;
        this.websocketSessionStats = subProtocolWebSocketHandler.getStats();
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        simpMessageSendingOperations.convertAndSend("/topic/lobbyUsers", websocketSessionStats.getWebSocketSessions());
    }
}
