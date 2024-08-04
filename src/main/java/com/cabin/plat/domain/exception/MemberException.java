package com.cabin.plat.domain.exception;

public class MemberException extends RuntimeException {
    private static final String ERROR_PREFIX = "[ERROR]";

    public MemberException(ErrorMessage errorMessage) {
        super(String.format("%s %s", ERROR_PREFIX, errorMessage.message));
    }
}
