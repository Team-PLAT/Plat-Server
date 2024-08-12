package com.cabin.plat.global.exception.errorCode;

import com.cabin.plat.global.exception.ErrorCode;
import com.cabin.plat.global.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCodeInterface {
    INVALID_TOKEN("AUTH001", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED)
    ,EXPIRED_TOKEN("AUTH002", "만료된 토큰입니다.", HttpStatus.UNAUTHORIZED)
    ,UNSUPPORTED_TOKEN("AUTH003", "지원되지 않는 토큰입니다.", HttpStatus.UNAUTHORIZED)
    ,WRONG_TOKEN_SIGNITURE("AUTH004", "토큰의 서명이 잘못됐습니다.", HttpStatus.UNAUTHORIZED)
    ,EMPTY_TOKEN("AUTH005", "토큰이 비어있습니다.", HttpStatus.UNAUTHORIZED)
    ,INVALID_TOKEN_TYPE("AUTH006", "유효하지 않은 토큰 타입입니다.", HttpStatus.UNAUTHORIZED)
    ,EXPIRED_SIGNUP_TOKEN("AUTH007", "만료된 회원가입 토큰입니다.", HttpStatus.UNAUTHORIZED)
    ,
    ;
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.builder()
                .code(code)
                .message(message)
                .httpStatus(httpStatus)
                .build();
    }
}
