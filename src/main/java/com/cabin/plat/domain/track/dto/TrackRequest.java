package com.cabin.plat.domain.track.dto;

import lombok.Builder;
import lombok.Getter;

public class TrackRequest {

    @Getter
    @Builder
    public static class TrackLike {
        private Boolean isLiked;
    }

    @Getter
    @Builder
    public static class TrackUpload {
        private String isrc;
        private String imageUrl;
        private String context;
        private Double latitude;
        private Double longitude;
    }
}
