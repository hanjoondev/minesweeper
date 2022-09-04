package com.zerobase.minesweeper.service;

import com.zerobase.minesweeper.dto.LoginResponse;
import com.zerobase.minesweeper.dto.TokenDto;
import com.zerobase.minesweeper.dto.TokensRequest;
import com.zerobase.minesweeper.entity.Gamer;
import com.zerobase.minesweeper.entity.RefreshToken;
import com.zerobase.minesweeper.exception.GamerException;
import com.zerobase.minesweeper.exception.JwtException;
import com.zerobase.minesweeper.repository.GamerRepository;
import com.zerobase.minesweeper.repository.RefreshTokenRepository;
import com.zerobase.minesweeper.security.TokenProvider;
import com.zerobase.minesweeper.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
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
                .orElseThrow(() -> new GamerException(ErrorCode.USER_NOT_FOUND));

        if (!gamer.isVerified()) {
            throw new GamerException(ErrorCode.NOT_AUTHENTICATED_EMAIL);
        }

        //회원 정지 시 로그인 불가!
        if (gamer.isSuspend()) {
            throw new GamerException(ErrorCode.USER_HAS_BEEN_BANNED);
        }

        return new User(gamer.getId().toString(), gamer.getPswd(),
                List.of(new SimpleGrantedAuthority(gamer.getRole().toString())));
    }

    /*
     *   db에서 멤버 확인
     *   토큰발급
     * */
    @Transactional
    public LoginResponse login(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        Authentication authentication = getAuthentication(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        Gamer gamer = getGamerByAuthentication(authentication);

        refreshTokenRepository.save(new RefreshToken(gamer.getId(), tokenDto.getRefreshToken()));
        return new LoginResponse(tokenDto, gamer.getId(), gamer.getName());
    }

    private Gamer getGamerByAuthentication(Authentication authentication) {
        return gamerRepository.findById(Long.valueOf(authentication.getName()))
                .orElseThrow(() -> new GamerException(ErrorCode.USER_NOT_FOUND));
    }

    private Authentication getAuthentication(UsernamePasswordAuthenticationToken authenticationToken) {
        try { // authenticate 호출 시 -> loadUserByUsername 실행
            return authenticationManagerBuilder.getObject()
                    .authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new GamerException(ErrorCode.INVALID_LOGIN_INFO);
        }
    }

    @Transactional
    public LoginResponse reissue(TokensRequest request) {
        if (!tokenProvider.validateToken(request.getRefreshToken())) {
            throw new JwtException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        Authentication authentication = tokenProvider.getAuthentication(request.getAccessToken());
        Gamer gamer = getGamerByAuthentication(authentication);

        RefreshToken refreshToken = refreshTokenRepository.findByGamerId(gamer.getId())
                                     .orElseThrow(() -> new JwtException(ErrorCode.NOT_FOUND_USER_TOKEN));
        if (!refreshToken.getToken().equals(request.getRefreshToken())) {
            throw new JwtException(ErrorCode.MIS_MATCH_TOKEN);
        }

        TokenDto tokenDto = tokenProvider.generateToken(authentication);
        refreshToken.updateToken(tokenDto.getRefreshToken()); // update refresh token in db

        return new LoginResponse(tokenDto, gamer.getId(), gamer.getName());
    }

    @Transactional
    public void logout(TokensRequest request) {
        Authentication authentication = tokenProvider.getAuthentication(request.getAccessToken());

        refreshTokenRepository.delete(refreshTokenRepository.findByGamerId(Long.valueOf(authentication.getName()))
                .orElseThrow(() -> new JwtException(ErrorCode.NOT_FOUND_USER_TOKEN)));
    }
}
