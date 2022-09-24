package com.zerobase.minesweeper.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler.*;

import com.zerobase.minesweeper.dto.ChatMessage;
import com.zerobase.minesweeper.dto.LobbyChatDto;
import com.zerobase.minesweeper.dto.LobbyChatResponse;
import com.zerobase.minesweeper.dto.LobbyChatStartResponse;
import com.zerobase.minesweeper.entity.LobbyChat;
import com.zerobase.minesweeper.repository.LobbyChatRepository;

import lombok.RequiredArgsConstructor;

@Service
public class LobbyChatService {
    private final LobbyChatRepository lobbyChatRepository;
    private final Stats websocketSessionStats;

    public LobbyChatService(LobbyChatRepository lobbyChatRepository, WebSocketHandler webSocketHandler) {
        this.lobbyChatRepository = lobbyChatRepository;
        SubProtocolWebSocketHandler subProtocolWebSocketHandler = (SubProtocolWebSocketHandler) webSocketHandler;
        this.websocketSessionStats = subProtocolWebSocketHandler.getStats();
    }

    private static final Queue<LobbyChatDto> chatCacheQueue = new ConcurrentLinkedQueue<>();
    private static final int CHAT_CACHE_SIZE = 30;
    private static final DateTimeFormatter customTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static long lastIndex;

    public LobbyChatStartResponse getStartChats() {
        return new LobbyChatStartResponse(new ArrayList<>(chatCacheQueue), lastIndex, websocketSessionStats.getWebSocketSessions() + 1);
    }

    public LobbyChatDto saveChat(ChatMessage chatMessage) {
        LobbyChatDto lobbyChatDto = LobbyChatDto.builder()
                                        .name(chatMessage.getName())
                                        .message(chatMessage.getMessage())
                                        .time(LocalDateTime.parse(LocalDateTime.now().format(customTimeFormatter), customTimeFormatter))
                                        .build();

        if (chatCacheQueue.size() >= CHAT_CACHE_SIZE) {
            lastIndex = lobbyChatRepository.save(LobbyChatDto.toEntity(Objects.requireNonNull(chatCacheQueue.poll())))
                            .getId();
        }

        chatCacheQueue.add(lobbyChatDto);

        return lobbyChatDto;
    }

    public LobbyChatResponse getOldChat(Long lastIndex) {
        List<LobbyChat> lobbyChats = lobbyChatRepository.findByIdBetween(Math.max(0, lastIndex - CHAT_CACHE_SIZE), lastIndex);
        if (lobbyChats.size() == 0) {
            return new LobbyChatResponse(Collections.emptyList(), lastIndex);
        }

        List<LobbyChatDto> lobbyChatDtos = lobbyChats
                        .stream()
                        .map(LobbyChatDto::fromEntity)
                        .collect(Collectors.toList());

        return new LobbyChatResponse(lobbyChatDtos, lobbyChats.get(lobbyChats.size() - 1).getId());
    }
}
