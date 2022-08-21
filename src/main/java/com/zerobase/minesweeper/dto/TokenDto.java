package com.zerobase.minesweeper.dto;

import lombok.Data;

@Data
public class TokenDto {
    private final String bearerType;
    private final String accessToken;
    private final String refreshToken;
    private final Long gamerId;
}
