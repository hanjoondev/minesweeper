package com.zerobase.minesweeper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenDto {
    private String bearerType;
    private String accessToken;
    private String refreshToken;
}
