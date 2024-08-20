package com.cabin.plat.domain.track.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

public class TrackResponse {

    @Getter
    @Builder
    public static class TrackMapList {
        private List<TrackMap> tracks;
    }

    @Getter
    @Builder
    public static class TrackDetailList {
        private List<TrackDetail> trackDetails;
    }

    @Getter
    @Builder
    private static class TrackMap {
        private Long trackId;
        private String isrc;
        private Boolean isLiked;
        private Double longitude;
        private Double latitude;
    }

    @Getter
    @Builder
    public static class TrackDetail {
        private Long trackId;
        private String isrc;
        private LocalDateTime createdAt;
        private Double longitude;
        private Double latitude;
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
    private static class MemberInfo {
        private Long memberId;
        private String memberNickname;
        private String avatar;
    }

    @Getter
    @Builder
    public static class TrackId {
        private Long trackId;
    }

    @Getter
    @Builder
    public static class ReportId {
        private Long reportId;
    }
}
