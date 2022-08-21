package com.zerobase.minesweeper.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /*  토큰 값이 유효하지 않을 때 401반환
    * */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        System.out.println("JwtAuthenticationEntryPoint.commence -> "+authException.getMessage());

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
