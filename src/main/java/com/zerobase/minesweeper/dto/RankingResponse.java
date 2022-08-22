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
public class RankingResponse {
    String difficulty;
    Boolean firstPage;
    Boolean lastPage;
    Integer numberOfElements;
    Long totalElements;
    List<Rank> contents;
}
