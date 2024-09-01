package com.cabin.plat.domain.track.service;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.member.repository.MemberRepository;
import com.cabin.plat.domain.track.dto.TrackRequest;
import com.cabin.plat.domain.track.dto.TrackResponse;
import com.cabin.plat.domain.track.dto.TrackResponse.*;
import com.cabin.plat.domain.track.entity.Track;
import com.cabin.plat.domain.track.mapper.TrackMapper;
import com.cabin.plat.domain.track.repository.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;
    private final MemberRepository memberRepository;
    private final LocationRepository locationRepository;
    private final TrackLikeRepository trackLikeRepository;
    private final TrackReportRepository trackReportRepository;
    private final TrackMapper trackMapper;

    @Override
    public TrackResponse.TrackMapList getTracksByLocation(Member member, double startLatitude, double startLongitude,
                                                          double endLatitude, double endLongitude) {
        double minLatitude = Math.min(startLatitude, endLatitude);
        double maxLatitude = Math.max(startLatitude, endLatitude);
        double minLongitude = Math.min(startLongitude, endLongitude);
        double maxLongitude = Math.max(startLongitude, endLongitude);

        List<Track> tracks = trackRepository.findAllTracksWithinBounds(
                minLatitude, minLongitude, maxLatitude, maxLongitude);

        List<TrackResponse.TrackMap> trackMaps = tracks.stream()
                .map(track -> trackMapper.toTrackMap(
                        track.getId(),
                        track.getIsrc(),
                        trackLikeRepository.existsByMemberAndTrack(member, track),
                        track.getLocation().getLongitude(),
                        track.getLocation().getLatitude()))
                .toList();

        return trackMapper.toTrackMapList(trackMaps);
    }

    @Override
    public TrackResponse.TrackDetail getTrackById(Member member, Long trackId) {
        return null;
    }

    @Override
    public TrackResponse.TrackId likeTrack(Member member, Long trackId, Boolean isLiked) {
        return null;
    }

    @Override
    public TrackResponse.TrackId addTrack(Member member, TrackRequest.TrackUpload trackUpload) {
        return null;
    }

    @Override
    public TrackResponse.TrackDetailList getTrackFeeds(Member member) {
        return null;
    }

    @Override
    public TrackResponse.ReportId reportTrack(Member member, Long trackId) {
        return null;
    }
}
