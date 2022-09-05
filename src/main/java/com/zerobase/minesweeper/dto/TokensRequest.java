package com.zerobase.minesweeper.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class TokensRequest {
    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;
}
