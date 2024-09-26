package com.cabin.plat.global.exception.errorCode;

import com.cabin.plat.global.exception.ErrorCode;
import com.cabin.plat.global.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum PlaylistErrorCode implements ErrorCodeInterface{
    PLAYLIST_NOT_FOUND("PLAYLIST001", "Playlist가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    PLAYLIST_DELETE_FORBIDDEN("PLAYLIST002", "이 플레이리스트를 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    PLAYLIST_UPDATE_FORBIDDEN("PLAYLIST003", "이 플레이리스트를 업데이트할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    PLAYLIST_TRACK_NOT_FOUND("PLAYLIST004", "플레이리스트에 해당 트랙이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    PLAYLIST_TRACK_DELETE_FORBIDDEN("PLAYLIST005", "이 플레이리스트의 트랙을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    PLAYLIST_TRACK_DUPLICATE("PLAYLIST006", "이미 추가된 트랙입니다.", HttpStatus.BAD_REQUEST),
    PLAYLIST_TRACK_COUNT_MISMATCH("PLAYLIST007", "요청한 트랙 순서 정보의 개수와 플레이리스트에 존재하는 트랙 개수가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    TRACK_NOT_FOUND("PLAYLIST008", "플레이리스트 안에 Track이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    PLAYLIST_TRACK_ID_MISMATCH("PLAYLIST009", "플레이리스트에 존재하는 트랙 ID 들과 요청한 트랙 ID 들이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
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
