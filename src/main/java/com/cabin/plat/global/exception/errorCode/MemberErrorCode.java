package com.cabin.plat.global.exception.errorCode;

import com.cabin.plat.global.exception.ErrorCode;
import com.cabin.plat.global.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ErrorCodeInterface {

    MEMBER_NOT_FOUND("MEMBER001", "Member가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    EXIST_MEMBER_NICKNAME("MEMBER002", "이미 사용 중인 닉네임입니다.",HttpStatus.BAD_REQUEST),
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
