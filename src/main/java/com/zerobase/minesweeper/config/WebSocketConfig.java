package com.zerobase.minesweeper.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final SubProtocolWebSocketHandler subProtocolWebSocketHandler;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    public WebSocketConfig(WebSocketHandler webSocketHandler, SimpMessageSendingOperations simpMessageSendingOperations) {
        this.subProtocolWebSocketHandler = (SubProtocolWebSocketHandler) webSocketHandler;
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/pub"); // MessageMapping 으로 갈 공통 상위 url prefix
    }

    @EventListener
    public void sessionConnectedEventHandler(SessionConnectedEvent event) {
        int webSocketSessions = subProtocolWebSocketHandler.getStats().getWebSocketSessions();
        simpMessageSendingOperations.convertAndSend("/topic/lobbyUsers", webSocketSessions);
    }

    @EventListener
    public void sessionDisconnectEventHandler(SessionDisconnectEvent event) {
        int webSocketSessions = subProtocolWebSocketHandler.getStats().getWebSocketSessions();
        simpMessageSendingOperations.convertAndSend("/topic/lobbyUsers", webSocketSessions);
    }

}
