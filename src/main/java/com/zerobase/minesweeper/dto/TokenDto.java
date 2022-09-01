package com.zerobase.minesweeper.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class TokenDto {
    private String bearerType;
    private String accessToken;
    private String refreshToken;
}
