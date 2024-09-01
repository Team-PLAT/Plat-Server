package com.cabin.plat.domain.track.mapper;

import com.cabin.plat.domain.member.dto.MemberResponse;
import com.cabin.plat.domain.track.dto.TrackResponse;
import com.cabin.plat.domain.track.dto.TrackResponse.TrackMap;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TrackMapper {
    public TrackResponse.TrackMap toTrackMap(Long trackId, String isrc, Boolean isLiked, Double longitude, Double latitude) {
        return TrackResponse.TrackMap.builder()
                .trackId(trackId)
                .isrc(isrc)
                .isLiked(isLiked)
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }

    public TrackResponse.TrackMapList toTrackMapList(List<TrackMap> tracks) {
        return TrackResponse.TrackMapList.builder()
                .tracks(tracks)
                .build();
    }

    public TrackResponse.TrackDetail toTrackDetail(Long trackId,
                                                   String isrc,
                                                   LocalDateTime createdAt,
                                                   Double latitude,
                                                   Double longitude,
                                                   String locationString,
                                                   String address,
                                                   String imageUrl,
                                                   String content,
                                                   int likeCount,
                                                   Boolean isLiked,
                                                   TrackResponse.MemberInfo memberInfo) {

        return TrackResponse.TrackDetail.builder()
                .trackId(trackId)
                .isrc(isrc)
                .createdAt(createdAt)
                .latitude(latitude)
                .longitude(longitude)
                .locationString(locationString)
                .address(address)
                .imageUrl(imageUrl)
                .context(content)
                .likeCount(likeCount)
                .isLiked(isLiked)
                .member(memberInfo)
                .build();
    }

    public TrackResponse.MemberInfo toMemberInfo(Long memberId, String memberNickname, String avatar) {
        return TrackResponse.MemberInfo.builder()
                .memberId(memberId)
                .memberNickname(memberNickname)
                .avatar(avatar)
                .build();
    }

    public TrackResponse.TrackDetailList toTrackDetailList(List<TrackResponse.TrackDetail> trackDetails) {
        return TrackResponse.TrackDetailList.builder()
                .trackDetails(trackDetails)
                .build();
    }

    public TrackResponse.TrackId toTrackId(Long trackId) {
        return TrackResponse.TrackId.builder()
                .trackId(trackId)
                .build();
    }

    public TrackResponse.ReportId toReportId(Long reportId) {
        return TrackResponse.ReportId.builder()
                .reportId(reportId)
                .build();
    }
}
