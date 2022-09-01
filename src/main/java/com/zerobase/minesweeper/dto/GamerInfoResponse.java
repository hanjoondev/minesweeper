package com.zerobase.minesweeper.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GamerInfoResponse {
    private Long id;
    private String name;
    private String mail;
    private boolean isVerified;
    private boolean isSuspend;
    private LocalDateTime regDt;
    private LocalDateTime verifiedDt;
}
