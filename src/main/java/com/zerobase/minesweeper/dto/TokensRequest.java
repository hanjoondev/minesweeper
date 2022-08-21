package com.zerobase.minesweeper.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TokensRequest {
    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;
}