package com.zerobase.minesweeper.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GamerActivationRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String validationKey;
}
