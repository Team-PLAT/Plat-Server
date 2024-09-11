package com.cabin.plat.domain.playlist.dto;

import java.util.List;
import lombok.*;

public class PlaylistRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlaylistUpload {
        private String title;
        private String playlistImageUrl;
        private List<TrackOrder> tracks;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        private static class TrackOrder {
            private Long trackId;
            private int orderIndex;
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrackId {
        private Long trackId;
    }
}
