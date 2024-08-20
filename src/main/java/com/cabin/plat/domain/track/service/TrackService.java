package com.cabin.plat.domain.track.service;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.track.dto.TrackResponse;
import com.cabin.plat.domain.track.dto.TrackResponse.TrackMapList;
import com.cabin.plat.global.common.BaseResponse;

public interface TrackService {
    TrackResponse.TrackMapList getTracksByLocation(Member member,
                                                   double startLatitude,
                                                   double startLongitude,
                                                   double endLatitude,
                                                   double endLongitude
    );

    TrackResponse.TrackDetail getTrackById(Member member, String trackId);

    TrackResponse.TrackId likeTrack(Member member, String trackId, Boolean isLiked);

    TrackResponse.TrackId addTrack(Member member,
                                   String isrc,
                                   String imageUrl,
                                   String context,
                                   double latitude,
                                   double longitude
    );

    TrackResponse.TrackDetailList getTrackFeeds(Member member);

    TrackResponse.ReportId reportTrack(Member member, String trackId);
}
