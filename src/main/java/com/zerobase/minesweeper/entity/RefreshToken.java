package com.zerobase.minesweeper.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RefreshToken {

    @Id
    private Long gamerId; //gamerId 외래키

    private String token; //refresh token

    public void updateToken(String token) {
        this.token = token;
    }

}
