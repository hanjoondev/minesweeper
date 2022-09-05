package com.zerobase.minesweeper.dto;

import java.time.LocalDateTime;

import com.zerobase.minesweeper.entity.LobbyChat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class LobbyChatDto {
    private String name;
    private String message;
    private LocalDateTime time;

    public static LobbyChatDto fromEntity(LobbyChat lobbyChat) {
        return LobbyChatDto.builder()
                .name(lobbyChat.getName())
                .message(lobbyChat.getMessage())
                .time(lobbyChat.getChatTime())
                .build();
    }

    public static LobbyChat toEntity(LobbyChatDto lobbyChatDto) {
        return LobbyChat.builder()
                .name(lobbyChatDto.getName())
                .message(lobbyChatDto.getMessage())
                .chatTime(lobbyChatDto.getTime())
                .build();
    }
}
