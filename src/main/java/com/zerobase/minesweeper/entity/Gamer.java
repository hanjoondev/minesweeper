package com.zerobase.minesweeper.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import com.zerobase.minesweeper.type.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Gamer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String mail;
    private String pswd;
    private String authCode;
    private boolean isVerified;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean isSuspend;
    @CreationTimestamp
    private LocalDateTime regDt;
    private LocalDateTime verifiedDt;
}
