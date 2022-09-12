package com.zerobase.minesweeper.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerobase.minesweeper.dto.LoginRequest;
import com.zerobase.minesweeper.dto.LoginResponse;
import com.zerobase.minesweeper.dto.TokensRequest;
import com.zerobase.minesweeper.service.AuthService;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request.getEmail(), request.getPassword()));
    }

    @PostMapping("/reissue")
    public ResponseEntity<LoginResponse> reissue(@Valid @RequestBody TokensRequest request) {
        return ResponseEntity.ok(authService.reissue(request));
    }

    // db에서 refresh token 삭제 후 프론트에서도 토큰 삭제
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody TokensRequest request) {
        authService.logout(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
