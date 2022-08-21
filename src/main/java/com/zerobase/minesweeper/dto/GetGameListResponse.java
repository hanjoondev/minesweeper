package com.zerobase.minesweeper.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetGameListResponse {
    Long gamerId;
    List<Long> gameList;
    List<String> difficultyList;
    List<Double> timeList;
    List<String> createdAtList;
    @Builder.Default
    Integer errorCode = 0;
}
