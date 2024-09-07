package com.cabin.plat.domain.track.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(description = "isrc", defaultValue = "BRBMG0300729")
        private String isrc;

        @Schema(description = "트랙 이미지 URL", defaultValue = "https://example.com/track.png")
        private String imageUrl;

        @Schema(description = "트랙 게시물 본문", defaultValue = "트랙 게시물 본문입니다.")
        private String content;

        @Schema(description = "트랙 위도 값", defaultValue = "36.014188")
        private double latitude;

        @Schema(description = "트랙 경도 값", defaultValue = "129.325802")
        private double longitude;
    }
}
