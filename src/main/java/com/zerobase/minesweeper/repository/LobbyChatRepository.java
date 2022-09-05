package com.zerobase.minesweeper.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerobase.minesweeper.entity.LobbyChat;

public interface LobbyChatRepository extends JpaRepository<LobbyChat, Long> {
    List<LobbyChat> findByIdBetween(Long startId, Long endId);
}
