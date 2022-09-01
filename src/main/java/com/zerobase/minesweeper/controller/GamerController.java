package com.zerobase.minesweeper.controller;

import com.zerobase.minesweeper.dto.*;
import com.zerobase.minesweeper.service.GamerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

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
        gamerService.updateGamerName(request.getEmail(), request.getName());
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

    /*
    관리자 페이지 용
    */

    //회원 목록 보기
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/gamers")
    public ResponseEntity<List<GamersBoardInfoResponse>> getGamers() {
        return ResponseEntity.ok(gamerService.getGamers().stream()
                .map(gamerDto -> GamersBoardInfoResponse.builder()
                        .id(gamerDto.getId())
                        .name(gamerDto.getName())
                        .mail(gamerDto.getMail())
                        .regDt(gamerDto.getRegDt().toLocalDate())
                        .build())
                .collect(Collectors.toList()));
    }

    //특정 회원 보기
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/gamer/{gamerId}")
    public ResponseEntity<GamerInfoResponse> getGamerInfo(@PathVariable("gamerId") @NotBlank Long gamerId) {
        GamerDto gamerDto = gamerService.getGamerInfo(gamerId);
        return ResponseEntity.ok(
                GamerInfoResponse.builder()
                        .id(gamerDto.getId())
                        .name(gamerDto.getName())
                        .mail(gamerDto.getMail())
                        .isVerified(gamerDto.isVerified())
                        .regDt(gamerDto.getRegDt())
                        .verifiedDt(gamerDto.getVerifiedDt())
                        .build()
        );
    }

    //회원 삭제
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/gamer/{gamerId}")
    public ResponseEntity<?> deleteGamer(@PathVariable("gamerId") @NotBlank Long gamerId) {
        gamerService.deleteGamer(gamerId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //회원 정지
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/gamer/{gamerId}/suspension/{suspend}")
    public ResponseEntity<?> suspendGamer(@PathVariable("gamerId") @NotBlank Long gamerId,
                                          @PathVariable("suspend") @NotBlank boolean suspend) {
        gamerService.suspendGamer(gamerId, suspend);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //회원 검색
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/gamer/search")
    public ResponseEntity<List<GamersBoardInfoResponse>> searchGamers(
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(gamerService.searchGamers(keyword).stream()
                .map(gamerDto -> GamersBoardInfoResponse.builder()
                        .id(gamerDto.getId())
                        .name(gamerDto.getName())
                        .regDt(gamerDto.getRegDt().toLocalDate())
                        .build())
                .collect(Collectors.toList()));
    }

}

