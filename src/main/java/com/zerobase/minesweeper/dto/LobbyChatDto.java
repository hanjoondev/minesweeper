package com.zerobase.minesweeper.dto;

import com.zerobase.minesweeper.entity.LobbyChat;
import lombok.*;

import java.time.LocalDateTime;

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
