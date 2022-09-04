package com.zerobase.minesweeper.exception;

import com.zerobase.minesweeper.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(GamerException.class)
    public ErrorResponse handleGamerException(GamerException e) {
        return new ErrorResponse(e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JwtException.class)
    public ErrorResponse handelJwtException(JwtException e) {
        return new ErrorResponse(e.getErrorCode(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(GameException.class)
    public ErrorResponse handleGameException(GameException e) {
        return new ErrorResponse(e.getErrorCode(), e.getErrorMessage());
    }

}
