package com.zerobase.minesweeper.exception;

import com.zerobase.minesweeper.dto.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GamerException.class)
    public ErrorResponse handleAccountException(GamerException e) {
        return new ErrorResponse(e.getErrorCode(), e.getErrorMessage());
    }
}
