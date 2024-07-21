package com.cabin.plat.domain.exception;

public enum ErrorMessage {
    GET_MEMBER_EXCEPTION("MEMBER id 로 찾기 실패."),
    TOKEN_AUTH_ERROR("유효하지 않은 토큰입니다.");

    public final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
