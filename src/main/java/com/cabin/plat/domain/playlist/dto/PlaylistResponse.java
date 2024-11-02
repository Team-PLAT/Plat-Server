package com.cabin.plat.domain.playlist.dto;

import com.cabin.plat.domain.track.dto.TrackResponse.TrackDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PlaylistResponse {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlayListId {
        @Schema(description = "플레이리스트 고유 ID", example = "1")
        private Long playlistId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Playlists {
        private List<PlaylistInfo> playlists;
        private boolean hasNext;

        @Getter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class PlaylistInfo {
            @Schema(description = "플레이리스트 고유 ID", example = "1")
            private Long playlistId;

            @Schema(description = "플레이리스트 제목", example = "플레이리스트 제목")
            private String title;

            @Schema(description = "플레이리스트 이미지 URL", example = "https://s3.amazonaws.com/mybucket/images/sample.jpg")
            private String playlistImageUrl;

            @Schema(description = "플레이리스트 생성일", example = "2024-09-22T07:23:09.102Z")
            private LocalDateTime createdAt;

            private Set<String> uploaderNicknames;
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlaylistDetail {
        @Schema(description = "플레이리스트 고유 ID", example = "1")
        private Long playlistId;

        @Schema(description = "플레이리스트 제목", example = "플레이리스트 제목")
        private String title;

        @Schema(description = "플레이리스트 이미지 URL", example = "https://s3.amazonaws.com/mybucket/images/sample.jpg")
        private String playlistImageUrl;

        @Schema(description = "플레이리스트 생성일", example = "2024-09-22T07:23:09.102Z")
        private LocalDateTime createdAt;

        private List<TrackDetailOrder> tracks;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrackDetailOrder {
        @Schema(description = "트랙 순서", example = "1")
        private int orderIndex;

        @Schema(description = "트랙 상세 정보")
        private TrackDetail trackDetail;
    }
}
