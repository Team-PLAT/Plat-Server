package com.cabin.plat.config.image.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class ImageResponse {
    @Getter
    @Builder
    public static class Avatar {
        @Schema(description = "아바타 이미지 URL", example = "https://example.com/avatar.png")
        private String avatar;
    }
}
