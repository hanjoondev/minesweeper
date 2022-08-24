package com.zerobase.minesweeper.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.zerobase.minesweeper.entity.Game;

public interface GameRepository extends PagingAndSortingRepository<Game, Long> {
    List<Game> findFirst10ByGamerIdOrderByCreatedAtDesc(Long gamerId);
    Page<Game> findByDifficulty(String difficulty, Pageable pageable);
    Long countByGamerId(Long gamerId);
}
