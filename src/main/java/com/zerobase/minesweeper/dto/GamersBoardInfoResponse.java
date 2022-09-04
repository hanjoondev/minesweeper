package com.zerobase.minesweeper.dto;

import lombok.*;

import java.time.LocalDate;

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
