package com.zerobase.minesweeper.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerobase.minesweeper.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByGamerId(Long gamerId);
}
