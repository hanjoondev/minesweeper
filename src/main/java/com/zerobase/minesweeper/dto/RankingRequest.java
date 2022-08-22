package com.zerobase.minesweeper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankingRequest {
    String difficulty;
    Integer pageIdx;
    Integer pageSize;
}
