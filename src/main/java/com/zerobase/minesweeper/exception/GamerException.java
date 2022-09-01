package com.zerobase.minesweeper.exception;

import com.zerobase.minesweeper.type.ErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GamerException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;

    public GamerException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
