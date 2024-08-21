package com.cabin.plat.global.exception.errorCode;

import com.cabin.plat.global.exception.ErrorCode;
import com.cabin.plat.global.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum S3ErrorCode implements ErrorCodeInterface {
    // Image
    FAILED_UPLOAD_S3_IMAGE(HttpStatus.INTERNAL_SERVER_ERROR, "IMAGE001", "S3에 이미지 저장에 실패하였습니다."),
    FALIED_READ_IMAGE(HttpStatus.BAD_REQUEST, "IMAGE002","이미지 파일을 읽는 중 문제가 발생하였습니다."),

    // File
    FAILED_UPLOAD_S3_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "FILE001", "S3에 파일 저장에 실패하였습니다."),
    FALIED_READ_FILE(HttpStatus.BAD_REQUEST, "FILE002","파일을 읽는 중 문제가 발생하였습니다."),

            ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.builder()
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
}
