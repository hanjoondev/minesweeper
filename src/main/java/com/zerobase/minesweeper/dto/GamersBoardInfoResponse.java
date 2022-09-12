package com.zerobase.minesweeper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GamersBoardInfoResponse {
    private Long id;
    private String name;
    private String mail;
    private boolean isVerified;
    private boolean isSuspend;
}
