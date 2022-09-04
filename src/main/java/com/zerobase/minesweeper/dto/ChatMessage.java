package com.zerobase.minesweeper.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChatMessage {
    @NotBlank
    private String name;
    @NotBlank
    private String message;
}
