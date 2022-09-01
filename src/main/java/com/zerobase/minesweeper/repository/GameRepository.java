package com.zerobase.minesweeper.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerobase.minesweeper.entity.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findFirst10ByGamerIdOrderByCreatedAtDesc(Long gamerId);
}
