package com.zerobase.minesweeper.repository;

import com.zerobase.minesweeper.entity.LobbyChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LobbyChatRepository extends JpaRepository<LobbyChat, Long> {
    List<LobbyChat> findByIdBetween(Long startId, Long endId);
}
