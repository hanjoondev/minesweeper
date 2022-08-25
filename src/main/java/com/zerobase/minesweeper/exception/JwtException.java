package com.zerobase.minesweeper.exception;

import com.zerobase.minesweeper.type.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtException extends RuntimeException {
    private final ErrorCode errorCode;

    public JwtException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }
}