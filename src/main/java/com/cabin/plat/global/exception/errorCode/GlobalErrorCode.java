package com.cabin.plat.global.exception.errorCode;

import com.cabin.plat.global.exception.ErrorCode;
import com.cabin.plat.global.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements ErrorCodeInterface {
    BAD_REQUEST("GLOBAL001", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    NOT_SUPPORTED_URI_ERROR("GLOBAL002", "올바르지 않은 URI입니다.", HttpStatus.NOT_FOUND),
    NOT_SUPPORTED_METHOD_ERROR("GLOBAL003", "지원하지 않는 Method입니다.", HttpStatus.METHOD_NOT_ALLOWED),
    NOT_SUPPORTED_MEDIA_TYPE_ERROR("GLOBAL004", "지원하지 않는 Media type입니다.", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    SERVER_ERROR("GLOBAL005", "서버 에러, 관리자에게 문의해주세요.", HttpStatus.INTERNAL_SERVER_ERROR),
    ACCESS_DENIED("GLOBAL006", "올바르지 않은 권한입니다.", HttpStatus.FORBIDDEN),
    NOT_VALID_ARGUMENT_ERROR("GLOBAL007", "올바르지 않은 Argument Type입니다.", HttpStatus.BAD_REQUEST),
    VALIDATION_ERROR("GLOBAL007", "Validation Error입니다.", HttpStatus.BAD_REQUEST),
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