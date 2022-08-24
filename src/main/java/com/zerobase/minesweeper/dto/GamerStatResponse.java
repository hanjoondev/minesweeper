package com.zerobase.minesweeper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GamerStatResponse {
    Long gamerId;
    Long clearCount;
    Long easyRank;
    Long mediumRank;
    Long hardRank;
    Double easyTime;
    Double mediumTime;
    Double hardTime;
    @Builder.Default
    Integer errorCode = 0;
}
