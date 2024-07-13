package com.cabin.plat.global.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ErrorCode {
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
