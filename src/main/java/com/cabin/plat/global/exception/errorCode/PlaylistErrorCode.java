package com.cabin.plat.global.exception.errorCode;

import com.cabin.plat.global.exception.ErrorCode;
import com.cabin.plat.global.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum PlaylistErrorCode implements ErrorCodeInterface{
    PLAYLIST_NOT_FOUND("PLAYLIST001", "플레이리스트가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    PLAYLIST_FORBIDDEN("PLAYLIST002", "이 플레이리스트 수정에 권한이 없는 유저입니다.", HttpStatus.FORBIDDEN),
    PLAYLIST_TRACK_NOT_FOUND("PLAYLIST003", "플레이리스트에 해당 트랙이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    PLAYLIST_TRACK_DUPLICATE("PLAYLIST004", "이미 추가된 트랙입니다.", HttpStatus.BAD_REQUEST),
    PLAYLIST_TRACK_COUNT_MISMATCH("PLAYLIST005", "요청한 트랙 순서 정보의 개수와 플레이리스트에 존재하는 트랙 개수가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    PLAYLIST_TRACK_ID_MISMATCH("PLAYLIST006", "플레이리스트에 존재하는 트랙들의 정보와 요청한 트랙들의 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    PLAYLIST_TRACK_ORDER_MISMATCH("PLAYLIST007", "요청한 트랙들의 순서 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    PlaylistErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public ErrorCode getErrorCode() {
        return ErrorCode.builder()
                .code(code)
                .message(message)
                .httpStatus(httpStatus)
                .build();
    }
}
