package com.zerobase.minesweeper.service;

import com.zerobase.minesweeper.dto.ChatMessage;
import com.zerobase.minesweeper.dto.LobbyChatDto;
import com.zerobase.minesweeper.dto.LobbyChatResponse;
import com.zerobase.minesweeper.entity.LobbyChat;
import com.zerobase.minesweeper.repository.LobbyChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LobbyChatService {
    private final LobbyChatRepository lobbyChatRepository;

    private static final Queue<LobbyChatDto> chatCacheQueue = new ConcurrentLinkedQueue<>();
    private static long lastIndex;
    private static final int CHAT_CACHE_SIZE = 30;
    private static final DateTimeFormatter customTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LobbyChatResponse getRecentlyChats() {
        return new LobbyChatResponse(new ArrayList<>(chatCacheQueue), lastIndex);
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
        List<LobbyChatDto> lobbyChatDtos = lobbyChats
                        .stream()
                        .map(LobbyChatDto::fromEntity)
                        .collect(Collectors.toList());

        return new LobbyChatResponse(lobbyChatDtos, lobbyChats.get(lobbyChats.size() - 1).getId());
    }
}
