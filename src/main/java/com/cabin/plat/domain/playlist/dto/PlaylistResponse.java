package com.cabin.plat.domain.playlist.dto;

import com.cabin.plat.domain.track.dto.TrackResponse.TrackDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

public class PlaylistResponse {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlayListId {
        @Schema(description = "플레이리스트 ID")
        private Long playlistId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Playlists {
        private List<PlaylistInfo> playlists;

        @Getter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        private static class PlaylistInfo {
            private Long playlistId;
            private String title;
            private String playlistImageUrl;
            private LocalDateTime createdAt;
            private List<String> uploaderNicknames;
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlaylistDetail {
        private Long playlistId;
        private String title;
        private String playlistImageUrl;
        private LocalDateTime createdAt;
        private List<TrackOrder> tracks;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrackOrder {
        @Schema(description = "트랙 순서", example = "1")
        private int orderIndex;

        @Schema(description = "트랙 상세 정보")
        private TrackDetail trackDetail;
    }
}
