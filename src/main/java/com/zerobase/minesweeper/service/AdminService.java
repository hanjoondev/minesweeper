package com.zerobase.minesweeper.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.zerobase.minesweeper.dto.GamerDto;
import com.zerobase.minesweeper.entity.Game;
import com.zerobase.minesweeper.entity.Gamer;
import com.zerobase.minesweeper.exception.GameException;
import com.zerobase.minesweeper.exception.GamerException;
import com.zerobase.minesweeper.repository.GameRepository;
import com.zerobase.minesweeper.repository.GamerRepository;
import com.zerobase.minesweeper.type.ErrorCode;
import com.zerobase.minesweeper.type.Role;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final GamerRepository gamerRepository;
    private final GameRepository gameRepository;

    @Transactional
    public List<GamerDto> getGamers() {

        List<Gamer> gamers = gamerRepository.findAll();

        return gamers.stream()
                .filter(gamer -> gamer.getMail() != null && gamer.getRole().equals(Role.ROLE_USER))
                .map(gamer -> GamerDto.builder()
                        .id(gamer.getId())
                        .name(gamer.getName())
                        .mail(gamer.getMail())
                        .isVerified(gamer.isVerified())
                        .isSuspend(gamer.isSuspend())
                        .regDt(gamer.getRegDt())
                        .verifiedDt(gamer.getVerifiedDt())
                        .build())
                .collect(Collectors.toList());


    }

    @Transactional
    public GamerDto getGamerInfo(Long gamerId) {

        Gamer gamer = gamerRepository.findById(gamerId).orElseThrow(() -> new GamerException(ErrorCode.USER_NOT_FOUND));

        return GamerDto.builder()
                .id(gamer.getId())
                .name(gamer.getName())
                .mail(gamer.getMail())
                .isVerified(gamer.isVerified())
                .isSuspend(gamer.isSuspend())
                .regDt(gamer.getRegDt())
                .verifiedDt(gamer.getVerifiedDt())
                .build();
    }

    @Transactional
    public void deleteGamer(Long gamerId) {

        Gamer gamer = gamerRepository.findById(gamerId).orElseThrow(() -> new GamerException(ErrorCode.USER_NOT_FOUND));

        gamerRepository.delete(gamer);

    }

    @Transactional
    public void suspendGamer(Long gamerId, boolean suspend) {

        Gamer gamer = gamerRepository.findById(gamerId).orElseThrow(() -> new GamerException(ErrorCode.USER_NOT_FOUND));

        gamer.setSuspend(suspend);

        gamerRepository.save(gamer);

    }

    @Transactional
    public List<GamerDto> searchGamers(String keyword) {

        List<Gamer> gamers = gamerRepository.findByNameOrMailContains(keyword, keyword);

        return gamers.stream()
                .filter(gamer -> !gamer.getMail().isBlank() && gamer.getRole().equals(Role.ROLE_USER))
                .map(gamer -> GamerDto.builder()
                        .id(gamer.getId())
                        .name(gamer.getName())
                        .mail(gamer.getMail())
                        .isVerified(gamer.isVerified())
                        .isSuspend(gamer.isSuspend())
                        .regDt(gamer.getRegDt())
                        .verifiedDt(gamer.getVerifiedDt())
                        .build())
                .collect(Collectors.toList());

    }

    @Transactional
    public boolean deleteGame(String gameId) {

        Long id = Long.valueOf(gameId);
        Game game = gameRepository.findById(id).orElseThrow(() -> new GameException(ErrorCode.GAME_NOT_FOUND));

        gameRepository.delete(game);

        return true;
    }
}
