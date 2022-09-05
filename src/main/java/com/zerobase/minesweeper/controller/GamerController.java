package com.zerobase.minesweeper.controller;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zerobase.minesweeper.dto.GamerActivationRequest;
import com.zerobase.minesweeper.dto.GamerLostPasswordRequest;
import com.zerobase.minesweeper.dto.GamerReissueValidationKeyRequest;
import com.zerobase.minesweeper.dto.GamerSignUpRequest;
import com.zerobase.minesweeper.dto.GamerUpdateNameRequest;
import com.zerobase.minesweeper.dto.GamerUpdatePasswordRequest;
import com.zerobase.minesweeper.dto.GamerWithdrawalRequest;
import com.zerobase.minesweeper.service.GamerService;

import lombok.RequiredArgsConstructor;

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
    public ResponseEntity<?> gamerSignUp(@RequestBody @Valid GamerSignUpRequest request) {
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

    //회원정보 수정 (닉네임)
    @PatchMapping("/gamer/name")
    public ResponseEntity<?> updateGamerName(@RequestBody @Valid GamerUpdateNameRequest request) {
        gamerService.updateGamerName(request.getEmail(), request.getName(), request.getPassword());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //회원 탈퇴
    @DeleteMapping("/gamer")
    public ResponseEntity<?> withdrawalGamer(@RequestBody @Valid GamerWithdrawalRequest request) {
        gamerService.withdrawalGamer(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //비밀번호 변경
    @PatchMapping("/gamer/password")
    public ResponseEntity<?> updateGamerPassword(@RequestBody @Valid GamerUpdatePasswordRequest request) {
        gamerService.updateGamerPassword(request.getEmail(), request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //비밀번호 찾기(찾기를 요청할 경우, 무작위로 생성된 비밀번호를 사용자 이메일로 보내줌)
    @PostMapping("/gamer/password")
    public ResponseEntity<?> lostGamerPassword(@RequestBody @Valid GamerLostPasswordRequest request) {
        gamerService.lostGamerPassword(request.getEmail());
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
