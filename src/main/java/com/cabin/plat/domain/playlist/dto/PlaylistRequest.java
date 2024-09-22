package com.cabin.plat.domain.playlist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.*;

public class PlaylistRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlaylistUpload {
        @Schema(description = "플레이리스트 제목", example = "플레이리스트 제목")
        private String title;

        @Schema(description = "플레이리스트 이미지 URL", example = "https://s3.amazonaws.com/mybucket/images/sample.jpg")
        private String playlistImageUrl;

        private List<TrackOrder> tracks;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class TrackOrder {
            @Schema(description = "트랙 고유 ID", example = "1")
            private Long trackId;

            @Schema(description = "플레이리스트에서 트랙의 순서", example = "0")
            private int orderIndex;
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrackId {
        @Schema(description = "트랙 고유 ID", example = "1")
        private Long trackId;
    }
}
