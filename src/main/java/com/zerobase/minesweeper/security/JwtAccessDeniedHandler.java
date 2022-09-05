package com.zerobase.minesweeper.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.minesweeper.dto.ErrorResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.zerobase.minesweeper.type.ErrorCode.NEED_TO_ROLE_ADMIN;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;
    
    // 필요한 권한(role)이 없이 접근하려 할때 403반환
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.error("Jwt Access denied, cause : {}", accessDeniedException.getMessage());

        set403ErrorResponse(response);
    }

    private void set403ErrorResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        objectMapper.writeValue(response.getWriter(), new ErrorResponse(NEED_TO_ROLE_ADMIN, NEED_TO_ROLE_ADMIN.getDescription()));
    }
}
