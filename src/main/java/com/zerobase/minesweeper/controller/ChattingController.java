package com.zerobase.minesweeper.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.zerobase.minesweeper.dto.ChatMessage;
import com.zerobase.minesweeper.dto.LobbyChatDto;
import com.zerobase.minesweeper.dto.LobbyChatResponse;
import com.zerobase.minesweeper.service.LobbyChatService;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ChattingController {
    private final LobbyChatService lobbyChatService;

    @MessageMapping("/lobby")
    @SendTo("/topic/lobby")
    public LobbyChatDto lobbyChat(@Valid ChatMessage chatMessage) {
        return lobbyChatService.saveChat(chatMessage);
    }

    // 채팅 첫 입장
    @GetMapping("/chat-lobby")
    public ResponseEntity<LobbyChatResponse> getRecentlyChat() {
        return ResponseEntity.ok(lobbyChatService.getRecentlyChats());
    }

    // lastIndex 부터
    @GetMapping("/chat-lobby/{lastIndex}")
    public ResponseEntity<LobbyChatResponse> getPagingChat(@PathVariable Long lastIndex) {
        return ResponseEntity.ok(lobbyChatService.getOldChat(lastIndex));
    }
}
