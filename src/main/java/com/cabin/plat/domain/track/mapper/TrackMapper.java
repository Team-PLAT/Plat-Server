package com.cabin.plat.domain.track.mapper;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.track.dto.TrackRequest;
import com.cabin.plat.domain.track.dto.TrackResponse;
import com.cabin.plat.domain.track.dto.TrackResponse.TrackMap;
import com.cabin.plat.domain.track.entity.*;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TrackMapper {
    public TrackResponse.TrackMap toTrackMap(
            Long trackId,
            String isrc,
            Boolean isLiked,
            Double latitude,
            Double longitude) {

        return TrackResponse.TrackMap.builder()
                .trackId(trackId)
                .isrc(isrc)
                .isLiked(isLiked)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    public TrackResponse.TrackMapList toTrackMapList(List<TrackMap> tracks) {
        return TrackResponse.TrackMapList.builder()
                .tracks(tracks)
                .build();
    }

    public TrackResponse.TrackDetail toTrackDetail(
            Long trackId,
            String isrc,
            LocalDateTime createdAt,
            Double latitude,
            Double longitude,
            String buildingName,
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
                .buildingName(buildingName)
                .address(address)
                .imageUrl(imageUrl)
                .content(content)
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

    public TrackLike toTrackLike(Member member, Track track) {
        return TrackLike.builder()
                .member(member)
                .track(track)
                .build();
    }

    public Location toLocation(String buildingName, String address, Double latitude, Double longitude) {
        return Location.builder()
                .buildingName(buildingName)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    public Track toTrack(Member member, Location location, TrackRequest.TrackUpload trackUpload) {
        return Track.builder()
                .member(member)
                .location(location)
                .isrc(trackUpload.getIsrc())
                .content(trackUpload.getContent())
                .imageUrl(trackUpload.getImageUrl())
                .build();
    }

    public TrackReport toTrackReport(Long trackId, Long memberId) {
        return TrackReport.builder()
                .reportTrackId(trackId)
                .reportMemberId(memberId)
                .build();
    }
}
