package com.zerobase.minesweeper.service;

import com.zerobase.minesweeper.dto.TokenDto;
import com.zerobase.minesweeper.dto.TokensRequest;
import com.zerobase.minesweeper.entity.Gamer;
import com.zerobase.minesweeper.entity.RefreshToken;
import com.zerobase.minesweeper.repository.GamerRepository;
import com.zerobase.minesweeper.repository.RefreshTokenRepository;
import com.zerobase.minesweeper.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthService implements UserDetailsService {
    private final GamerRepository gamerRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Gamer gamer = gamerRepository.findByMail(username)
                .orElseThrow(() -> new RuntimeException("읎는 아이디 입네다."));

        if (!gamer.isVerified()) {
            throw new RuntimeException("이메일 인증이 되지 않은 사용자입니다.");
        }

        return new User(gamer.getId().toString(), gamer.getPswd(),
                List.of(new SimpleGrantedAuthority(gamer.getRole().toString())));
    }

    /*
     *   db에서 멤버 확인
     *   토큰발급
     * */
    public TokenDto login(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        Authentication authentication = authenticationManagerBuilder.getObject()
                                        .authenticate(authenticationToken);// authenticate 호출 시 -> loadUserByUsername 실행

        SecurityContextHolder.getContext().setAuthentication(authentication);
        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        refreshTokenRepository.save(new RefreshToken(Long.parseLong(authentication.getName()), tokenDto.getRefreshToken()));
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokensRequest request) {
        if (!tokenProvider.validateToken(request.getRefreshToken())) {
            throw new RuntimeException("refresh token이 만료되었습니다. 재로그인이 필요합니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(request.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByGamerId(Long.valueOf(authentication.getName()))
                .orElseThrow(() -> new RuntimeException("로그아웃된 사용자입니다."));

        if (!refreshToken.getToken().equals(request.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        refreshToken.updateToken(tokenDto.getRefreshToken()); // update refresh token in db

        return tokenDto;
    }

    public boolean logout(TokensRequest request) {
        Authentication authentication = tokenProvider.getAuthentication(request.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByGamerId(Long.valueOf(authentication.getName()))
                .orElseThrow(() -> new RuntimeException("저장된 토큰 값을 찾을 수 없습니다."));

        refreshTokenRepository.delete(refreshToken);
        return true;
    }
}
