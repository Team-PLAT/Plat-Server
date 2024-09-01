package com.cabin.plat.domain.track.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

public class TrackResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrackMapList {
        private List<TrackMap> tracks;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrackDetailList {
        private List<TrackDetail> trackDetails;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class TrackMap {
        private Long trackId;
        private String isrc;
        private Double latitude;
        private Double longitude;
        private Boolean isLiked;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrackDetail {
        private Long trackId;
        private String isrc;
        private LocalDateTime createdAt;
        private Double latitude;
        private Double longitude;
        private String locationString;
        private String address;
        private String imageUrl;
        private String context;
        private int likeCount;
        private Boolean isLiked;
        private MemberInfo member;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberInfo {
        private Long memberId;
        private String memberNickname;
        private String avatar;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrackId {
        private Long trackId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReportId {
        private Long reportId;
    }
}
