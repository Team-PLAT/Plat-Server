package com.cabin.plat.domain.track.service;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.member.repository.MemberRepository;
import com.cabin.plat.domain.track.dto.TrackRequest;
import com.cabin.plat.domain.track.dto.TrackResponse;
import com.cabin.plat.domain.track.entity.*;
import com.cabin.plat.domain.track.mapper.TrackMapper;
import com.cabin.plat.domain.track.repository.*;
import com.cabin.plat.global.exception.RestApiException;
import com.cabin.plat.global.exception.errorCode.TrackErrorCode;
import java.util.List;
import java.util.Optional;
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
        // 좌표 재정렬
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
        Track track = trackRepository.findById(trackId).orElseThrow(() -> new RestApiException(TrackErrorCode.TRACK_NOT_FOUND));
        TrackResponse.MemberInfo memberInfo = trackMapper.toMemberInfo(
                track.getMember().getId(),
                track.getMember().getNickname(),
                track.getMember().getAvatar()
        );

        return trackMapper.toTrackDetail(
                track.getId(),
                track.getIsrc(),
                track.getCreatedAt(),
                track.getLocation().getLatitude(),
                track.getLocation().getLongitude(),
                track.getLocation().getPlaceName(),
                track.getLocation().getAddress(),
                track.getImageUrl(),
                track.getContent(),
                trackLikeRepository.countByTrack(track),
                trackLikeRepository.existsByMemberAndTrack(member, track),
                memberInfo
        );

    }

    @Override
    public TrackResponse.TrackId likeTrack(Member member, Long trackId, Boolean isLiked) {
        Track track = trackRepository.findById(trackId).orElseThrow(() -> new RestApiException(TrackErrorCode.TRACK_NOT_FOUND));

        Optional<TrackLike> existingLike = trackLikeRepository.findByMemberAndTrack(member, track);

        if (isLiked) {
            if (existingLike.isEmpty()) {
                TrackLike trackLike = TrackLike.builder()
                        .member(member)
                        .track(track)
                        .build();
                trackLikeRepository.save(trackLike);
            }
        } else {
            existingLike.ifPresent(trackLikeRepository::delete);
        }

        return trackMapper.toTrackId(trackId);
    }

    @Override
    public TrackResponse.TrackId addTrack(Member member, TrackRequest.TrackUpload trackUpload) {
        Location location = locationRepository.save(Location.builder()
                .placeName("장소 이름 (미구현)") // TODO: 장소 이름
                .address("주소 (미구현)") // TODO: 위도 경도로 주소 받아오기
                .latitude(trackUpload.getLatitude())
                .longitude(trackUpload.getLongitude())
                .build());

        Track track = Track.builder()
                .member(member)
                .location(location)
                .isrc(trackUpload.getIsrc())
                .content(trackUpload.getContext())
                .imageUrl(trackUpload.getImageUrl())
                .build();

        Track savedTrack = trackRepository.save(track);

        return trackMapper.toTrackId(savedTrack.getId());
    }

    @Override
    public TrackResponse.TrackDetailList getTrackFeeds(Member member) {
        List<Track> tracks = trackRepository.findAll(); // TODO: 페이지네이션 또는 친구 트랙만 불러오기

        List<TrackResponse.TrackDetail> trackDetails = tracks.stream()
                .map(track -> {
                    TrackResponse.MemberInfo memberInfo = trackMapper.toMemberInfo(
                            track.getMember().getId(),
                            track.getMember().getNickname(),
                            track.getMember().getAvatar()
                    );

                    return trackMapper.toTrackDetail(
                            track.getId(),
                            track.getIsrc(),
                            track.getCreatedAt(),
                            track.getLocation().getLongitude(),
                            track.getLocation().getLatitude(),
                            track.getLocation().getPlaceName(),
                            track.getLocation().getAddress(),
                            track.getImageUrl(),
                            track.getContent(),
                            trackLikeRepository.countByTrack(track),
                            trackLikeRepository.existsByMemberAndTrack(member, track),
                            memberInfo
                    );
                })
                .collect(Collectors.toList());

        return trackMapper.toTrackDetailList(trackDetails);
    }

    @Override
    public TrackResponse.ReportId reportTrack(Member member, Long trackId) {
        Track track = trackRepository.findById(trackId).orElseThrow(() -> new RestApiException(TrackErrorCode.TRACK_NOT_FOUND));

        TrackReport trackReport = TrackReport.builder()
                .reportTrackId(trackId)
                .reportMemberId(member.getId())
                .build();

        TrackReport savedTrackReport = trackReportRepository.save(trackReport);

        return trackMapper.toReportId(savedTrackReport.getId());
    }
}
