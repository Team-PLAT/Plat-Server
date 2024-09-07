package com.cabin.plat.domain.track.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

    public class TrackResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrackMapList {
        @Schema(description = "트랙 리스트")
        private List<TrackMap> tracks;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrackDetailList {
        @Schema(description = "트랙 디테일 리스트")
        private List<TrackDetail> trackDetails;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class TrackMap {
        @Schema(description = "트랙 고유 ID", example = "1")
        private Long trackId;

        @Schema(description = "트랙 ISRC 코드", example = "isrc2")
        private String isrc;

        @Schema(description = "트랙의 위도", example = "36.015733")
        private Double latitude;

        @Schema(description = "트랙의 경도", example = "129.322700")
        private Double longitude;

        @Schema(description = "좋아요 여부", example = "false")
        private Boolean isLiked;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrackDetail {
        @Schema(description = "트랙 고유 ID", example = "1")
        private Long trackId;

        @Schema(description = "트랙 ISRC 코드", example = "BRBMG0300729")
        private String isrc;

        @Schema(description = "트랙 생성일", example = "2024-09-07T17:40:43.049879")
        private LocalDateTime createdAt;

        @Schema(description = "트랙의 위도", example = "36.014188")
        private Double latitude;

        @Schema(description = "트랙의 경도", example = "129.325802")
        private Double longitude;

        @Schema(description = "건물 이름", example = "포항공대제1융합관")
        private String buildingName;

        @Schema(description = "주소", example = "경상북도 포항시 남구 지곡동")
        private String address;

        @Schema(description = "트랙 이미지 URL", example = "https://example.com/track.png")
        private String imageUrl;

        @Schema(description = "트랙 내용", example = "트랙 게시물 본문입니다.")
        private String content;

        @Schema(description = "좋아요 수", example = "12")
        private int likeCount;

        @Schema(description = "좋아요 여부", example = "false")
        private Boolean isLiked;

        @Schema(description = "작성자 정보")
        private MemberInfo member;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberInfo {
        @Schema(description = "회원 고유 ID", example = "1")
        private Long memberId;

        @Schema(description = "회원 닉네임", example = "plat")
        private String memberNickname;

        @Schema(description = "회원 아바타 이미지 URL", example = "https://example.com/avatar.png")
        private String avatar;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrackId {
        @Schema(description = "트랙 고유 ID", example = "1")
        private Long trackId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReportId {
        @Schema(description = "리포트 고유 ID", example = "100")
        private Long reportId;
    }
}
