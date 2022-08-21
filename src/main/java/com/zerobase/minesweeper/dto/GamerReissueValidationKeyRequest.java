package com.zerobase.minesweeper.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GamerReissueValidationKeyRequest {
    @Email
    @NotBlank
    private String email;
}
