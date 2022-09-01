package com.zerobase.minesweeper.controller;

import com.zerobase.minesweeper.dto.*;
import com.zerobase.minesweeper.service.GamerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@RestController
@RequiredArgsConstructor
public class GamerController {

    private final GamerService gamerService;


    //이메일 중복 확인
    @GetMapping("/gamer/email/{email}")
    public boolean emailDuplicateCheck(@PathVariable("email") @Email @NotBlank String email) {
        return !gamerService.emailDuplicateCheck(email);
        // 프런트와 상의 필요
    }

    //회원 가입
    @PostMapping("/gamer")
    public boolean gamerSignUp(@RequestBody @Valid GamerSignInRequest request) {
        return gamerService.gamerSignUp(request.getEmail(), request.getName(), request.getPassword());
    }

    //회원 활성화
    @PostMapping("/gamer/validation")
    public boolean gamerActivation(@RequestBody @Valid GamerActivationRequest request) {
        return gamerService.gamerActivation(request.getEmail(), request.getValidationKey());
    }

    //인증번호 재발급
    @PatchMapping("/gamer/validation")
    public boolean reissueValidationKey(@RequestBody @Valid GamerReissueValidationKeyRequest request) {
        return gamerService.reissueValidationKey(request.getEmail());
    }

    //회원정보 수정
    @PatchMapping("/gamer")
    public boolean updateGamerInfo(@RequestBody @Valid GamerUpdateInfoRequest request){
        return gamerService.updateGamerInfo(request.getEmail(), request.getName());
    }

    //회원 탈퇴
    @DeleteMapping("/gamer")
    public boolean deleteGamer(@RequestBody @Valid GamerDeleteRequest request) {
        return gamerService.deleteGamer(request.getEmail(), request.getPassword());
    }

}

