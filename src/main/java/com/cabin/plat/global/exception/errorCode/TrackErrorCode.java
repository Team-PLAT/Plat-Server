package com.cabin.plat.global.exception.errorCode;

import com.cabin.plat.global.exception.ErrorCode;
import com.cabin.plat.global.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TrackErrorCode implements ErrorCodeInterface {

    TRACK_NOT_FOUND("TRACK001", "Track이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    TRACK_DELETE_FORBIDDEN("TRACK002", "이 트랙을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN),
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