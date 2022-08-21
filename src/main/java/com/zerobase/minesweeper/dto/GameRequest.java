package com.zerobase.minesweeper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameRequest {
    Long gamerId;
    Integer height;
    Integer width;
    Integer numMines;
    Double timePlayed;
    Integer[] nearbyMines;
    String steps;
    Boolean isAnon;
}
