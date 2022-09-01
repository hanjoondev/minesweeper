package com.zerobase.minesweeper.exception;

import com.zerobase.minesweeper.type.ErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;

    public GameException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
