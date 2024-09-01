package com.cabin.plat.domain.track.service;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.track.dto.TrackRequest;
import com.cabin.plat.domain.track.dto.TrackResponse;
import com.cabin.plat.domain.track.dto.TrackResponse.TrackMapList;
import com.cabin.plat.global.common.BaseResponse;

public interface TrackService {
    TrackResponse.TrackMapList getTracksByLocation(
            Member member,
            double startLatitude,
            double startLongitude,
            double endLatitude,
            double endLongitude
    );

    TrackResponse.TrackDetail getTrackById(Member member, Long trackId);

    TrackResponse.TrackId likeTrack(Member member, Long trackId, Boolean isLiked);

    TrackResponse.TrackId addTrack(Member member, TrackRequest.TrackUpload trackUpload);

    TrackResponse.TrackDetailList getTrackFeeds(Member member);

    TrackResponse.ReportId reportTrack(Member member, Long trackId);
}
