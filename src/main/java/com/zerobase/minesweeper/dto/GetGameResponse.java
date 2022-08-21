package com.zerobase.minesweeper.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetGameResponse {
    String difficulty;
    Double timePlayed;
    LocalDateTime createdAt;
    Integer[] nearbyMines;
    String steps;
    @Builder.Default
    Integer errorCode = 0;
}
