package com.zerobase.minesweeper.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zerobase.minesweeper.entity.Gamer;

@Repository
public interface GamerRepository extends JpaRepository<Gamer, Long> {
    boolean existsByMail(String mail);
    Optional<Gamer> findByMail(String mail);
    List<Gamer> findByNameOrMailContains(String name, String mail);

}
