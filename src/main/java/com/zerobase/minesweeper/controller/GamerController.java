package com.zerobase.minesweeper.controller;

import com.zerobase.minesweeper.dto.*;
import com.zerobase.minesweeper.service.GamerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> gamerSignUp(@RequestBody @Valid GamerSignInRequest request) {
        gamerService.gamerSignUp(request.getEmail(), request.getName(), request.getPassword());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //회원 활성화
    @PostMapping("/gamer/validation")
    public ResponseEntity<?> gamerActivation(@RequestBody @Valid GamerActivationRequest request) {
        gamerService.gamerActivation(request.getEmail(), request.getValidationKey());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //인증번호 재발급
    @PatchMapping("/gamer/validation")
    public ResponseEntity<?> reissueValidationKey(@RequestBody @Valid GamerReissueValidationKeyRequest request) {
        gamerService.reissueValidationKey(request.getEmail());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //회원정보 수정
    @PatchMapping("/gamer")
    public ResponseEntity<?> updateGamerInfo(@RequestBody @Valid GamerUpdateInfoRequest request){
        gamerService.updateGamerInfo(request.getEmail(), request.getName());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //회원 탈퇴
    @DeleteMapping("/gamer")
    public ResponseEntity<?> deleteGamer(@RequestBody @Valid GamerDeleteRequest request) {
        gamerService.deleteGamer(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(HttpStatus.OK);
    }

}

