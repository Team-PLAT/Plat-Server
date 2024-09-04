package com.cabin.plat.domain.track.dto;

import lombok.*;

public class TrackRequest {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrackLike {
        private Boolean isLiked;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrackUpload {
        private String isrc;
        private String imageUrl;
        private String content;
        private double latitude;
        private double longitude;
    }
}
