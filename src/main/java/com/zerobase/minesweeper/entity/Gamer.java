package com.zerobase.minesweeper.entity;

import com.zerobase.minesweeper.type.Role;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

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
