package com.zerobase.minesweeper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class LobbyChatStartResponse {
    private List<LobbyChatDto> chatList;
    private Long lastIndex;
    private int users;
}
