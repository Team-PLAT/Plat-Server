package com.cabin.plat.global.exception;

import com.cabin.plat.global.common.BaseResponse;
import com.cabin.plat.global.exception.errorCode.GlobalErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    /**
     * 정의한 RestApiException 예외 처리
     */
    @ExceptionHandler(value = RestApiException.class)
    public ResponseEntity<BaseResponse<String>> handleRestApiException(
            RestApiException e) {
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    /**
     * 일반적인 서버 에러 예외 처리
     */
    @ExceptionHandler
    public ResponseEntity<BaseResponse<String>> handleException(
            Exception e) {
        e.printStackTrace();
        return handleExceptionInternalFalse(GlobalErrorCode.SERVER_ERROR.getErrorCode(), e.getMessage());
    }

    /**
     * @Validated 검증 실패 예외 처리
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse<String>> handleConstraintViolationException(
            ConstraintViolationException e) {
        return handleExceptionInternal(GlobalErrorCode.VALIDATION_ERROR.getErrorCode());
    }

    /**
     * 메서드의 인자 타입이 예상과 다른 경우 예외 처리
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse<String>> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException e) {
        // 예외 처리 로직
        return handleExceptionInternal(GlobalErrorCode.NOT_VALID_ARGUMENT_ERROR.getErrorCode());
    }

    /**
     * @RequestBody 내부 처리 실패,
     * @Valid 검증 실패한 경우 예외 처리
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();

        e.getBindingResult().getFieldErrors().stream()
                .forEach(fieldError -> {
                    String fieldName = fieldError.getField();
                    String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
                    errors.merge(fieldName, errorMessage, (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", " + newErrorMessage);
                });

        return handleExceptionInternalArgs(GlobalErrorCode.VALIDATION_ERROR.getErrorCode(), errors);

    }

    private ResponseEntity<BaseResponse<String>> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), null));
    }

    private ResponseEntity<Object> handleExceptionInternalArgs(
            ErrorCode errorCode,
            Map<String, String> errorArgs) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), errorArgs));
    }
    private ResponseEntity<BaseResponse<String>> handleExceptionInternalFalse(
            ErrorCode errorCode,
            String errorPoint) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), errorPoint));
    }
}
