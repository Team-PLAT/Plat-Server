package com.cabin.plat.domain.track.service;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.track.dto.TrackRequest;
import com.cabin.plat.domain.track.dto.TrackResponse.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrackServiceImpl implements TrackService {
    @Override
    public TrackMapList getTracksByLocation(Member member, double startLatitude, double startLongitude,
                                            double endLatitude, double endLongitude) {
        return null;
    }

    @Override
    public TrackDetail getTrackById(Member member, String trackId) {
        return null;
    }

    @Override
    public TrackId likeTrack(Member member, String trackId, Boolean isLiked) {
        return null;
    }

    @Override
    public TrackId addTrack(Member member, TrackRequest.TrackUpload trackUpload) {
        return null;
    }

    @Override
    public TrackDetailList getTrackFeeds(Member member) {
        return null;
    }

    @Override
    public ReportId reportTrack(Member member, String trackId) {
        return null;
    }
}
