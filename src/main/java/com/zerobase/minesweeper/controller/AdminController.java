package com.zerobase.minesweeper.controller;

import com.zerobase.minesweeper.dto.GamerDto;
import com.zerobase.minesweeper.dto.GamerInfoResponse;
import com.zerobase.minesweeper.dto.GamersBoardInfoResponse;
import com.zerobase.minesweeper.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    //회원 목록 보기
    @GetMapping("/gamers")
    public ResponseEntity<List<GamersBoardInfoResponse>> getGamers() {
        return ResponseEntity.ok(adminService.getGamers().stream()
                .map(gamerDto -> GamersBoardInfoResponse.builder()
                        .id(gamerDto.getId())
                        .name(gamerDto.getName())
                        .mail(gamerDto.getMail())
                        .regDt(gamerDto.getRegDt().toLocalDate())
                        .build())
                .collect(Collectors.toList()));
    }

    //특정 회원 보기
    @GetMapping("/gamer/{gamerId}")
    public ResponseEntity<GamerInfoResponse> getGamerInfo(@PathVariable("gamerId") @NotBlank Long gamerId) {
        GamerDto gamerDto = adminService.getGamerInfo(gamerId);
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
    @DeleteMapping("/gamer/{gamerId}")
    public ResponseEntity<?> deleteGamer(@PathVariable("gamerId") @NotBlank Long gamerId) {
        adminService.deleteGamer(gamerId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //회원 정지
    @PatchMapping("/gamer/{gamerId}/suspension/{suspend}")
    public ResponseEntity<?> suspendGamer(@PathVariable("gamerId") @NotBlank Long gamerId,
                                          @PathVariable("suspend") @NotBlank boolean suspend) {
        adminService.suspendGamer(gamerId, suspend);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //회원 검색
    @GetMapping("/gamer/search")
    public ResponseEntity<List<GamersBoardInfoResponse>> searchGamers(
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(adminService.searchGamers(keyword).stream()
                .map(gamerDto -> GamersBoardInfoResponse.builder()
                        .id(gamerDto.getId())
                        .name(gamerDto.getName())
                        .mail(gamerDto.getMail())
                        .regDt(gamerDto.getRegDt().toLocalDate())
                        .build())
                .collect(Collectors.toList()));
    }

    //게임 삭제
    @DeleteMapping("game/{gameId}")
    public ResponseEntity<?> deleteGame(@PathVariable String gameId){
        adminService.deleteGame(gameId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
