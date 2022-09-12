package com.zerobase.minesweeper.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class LobbyChatResponse {
    private List<LobbyChatDto> chatList;
    private Long lastIndex;
}
