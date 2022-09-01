package com.zerobase.minesweeper.security;

import com.zerobase.minesweeper.dto.TokenDto;
import com.zerobase.minesweeper.exception.JwtException;
import com.zerobase.minesweeper.type.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Component
public class TokenProvider {
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String BEARER_TYPE = "Bearer";
    private static final String KEY_ROLES = "roles";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30M
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24; // 1D

    public TokenDto generateToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());// claim 데이터 추후 암호화 필요
        claims.put(KEY_ROLES, authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" ")));

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(SECRET_KEY)
                .compact();

        String refreshToken = Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(SECRET_KEY)
                .compact();

        return new TokenDto(BEARER_TYPE, accessToken, refreshToken);
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (ObjectUtils.isEmpty(claims.get(KEY_ROLES))) {
            throw new JwtException(ErrorCode.INVALID_TOKEN);
        }

        List<SimpleGrantedAuthority> authentication = Arrays.stream(claims.get(KEY_ROLES).toString().split(" "))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User user = new User(claims.getSubject(), "", authentication);

        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    public Long getGamerId(String token) {
        return Long.valueOf(parseClaims(token).getSubject());
    }

    public boolean validateToken(String token) {
        try {
            return parseClaims(token)
                    .getExpiration().after(new Date());
        } catch (Exception e) {
            log.error("token {} error : {}", token, e.getMessage());

            return false;
        }

    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (Exception e) {
            throw new JwtException(ErrorCode.INVALID_TOKEN);
        }
    }

}
