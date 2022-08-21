package com.zerobase.minesweeper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResponse {
    Long gameId;
    Long gamerId;
    @Builder.Default
    Integer errorCode = 0;
}
