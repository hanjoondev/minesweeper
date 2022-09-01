package com.zerobase.minesweeper.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND("사용자가 없습니다."),
    NOT_AUTHENTICATED_EMAIL("이메일 인증이 되지 않은 사용자입니다."),
    INVALID_LOGIN_INFO("아이디 또는 비밀번호가 일치하지 않습니다."),


    USER_ALREADY_VALIDATED("이미 이메일 인증이 된 사용자 입니다."),
    VALIDATION_KEY_MIS_MATCH("올바르지 않은 인증키 입니다."),
    PASSWORD_MIS_MATCH("올바르지 않은 비밀번호 입니다."),
    INVALID_LOGIN_INFO("아이디 또는 비밀번호가 일치하지 않습니다."),
    NOT_FOUND_USER_TOKEN("로그아웃된 사용자입니다."),
    MIS_MATCH_TOKEN("토큰 정보가 일치하지 않습니다."),
    INVALID_REFRESH_TOKEN("refresh token이 유효하지 않습니다. 재로그인이 필요합니다."),
    INVALID_TOKEN("token 값이 유효하지 않습니다."),
    NEED_TO_ROLE_ADMIN("관리자 권한이 필요합니다."),

    INTERNAL_SERVER_ERROR("서버에 이상이 생겼습니다")
    ;

    private final String description;
}
