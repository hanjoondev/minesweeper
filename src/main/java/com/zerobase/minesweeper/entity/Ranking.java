package com.zerobase.minesweeper.entity;

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
public class Ranking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long gamerId;
    String gamerName;
    Double easyTime;
    Long easyId;
    Double mediumTime;
    Long mediumId;
    Double hardTime;
    Long hardId;
}
