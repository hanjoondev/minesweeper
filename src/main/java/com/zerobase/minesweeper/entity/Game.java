package com.zerobase.minesweeper.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Integer height;
    Integer width;
    Integer numMines;
    Double timePlayed;
    @Column(length = 524288)
    Integer[] nearbyMines;
    @Column(length = 1048576)
    String steps;
    Integer score;
    String difficulty;
    Boolean isAnon;
    Long gamerId;
    @CreationTimestamp
    LocalDateTime createdAt;
}
