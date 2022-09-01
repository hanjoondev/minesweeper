package com.zerobase.minesweeper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private TokenDto tokenDto;
    private Long gamerId;
    private String gamerName;
}
