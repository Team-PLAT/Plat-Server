package com.cabin.plat.domain.track.service;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.track.dto.TrackRequest;
import com.cabin.plat.domain.track.dto.TrackResponse;
import com.cabin.plat.domain.track.dto.TrackResponse.TrackDetail;
import com.cabin.plat.domain.track.entity.*;
import com.cabin.plat.domain.track.mapper.TrackMapper;
import com.cabin.plat.domain.track.repository.*;
import com.cabin.plat.global.exception.RestApiException;
import com.cabin.plat.global.exception.errorCode.TrackErrorCode;
import com.cabin.plat.global.util.geocoding.AddressInfo;
import com.cabin.plat.global.util.geocoding.ApiKeyProperties;
import com.cabin.plat.global.util.geocoding.ReverseGeoCoding;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TrackServiceImpl implements TrackService {
    private final TrackRepository trackRepository;
    private final LocationRepository locationRepository;
    private final TrackLikeRepository trackLikeRepository;
    private final TrackReportRepository trackReportRepository;
    private final TrackMapper trackMapper;
    private final ReverseGeoCoding reverseGeoCoding;

    @Override
    public TrackResponse.TrackMapList getTracksByLocation(
            Member member,
            double startLatitude,
            double startLongitude,
            double endLatitude,
            double endLongitude) {

        // 좌표 재정렬
        double minLatitude = Math.min(startLatitude, endLatitude);
        double maxLatitude = Math.max(startLatitude, endLatitude);
        double minLongitude = Math.min(startLongitude, endLongitude);
        double maxLongitude = Math.max(startLongitude, endLongitude);

        List<Track> tracks = trackRepository.findAllTracksWithinBounds(
                minLatitude,
                maxLatitude,
                minLongitude,
                maxLongitude);

        List<TrackResponse.TrackMap> trackMaps = tracks.stream()
                .map(track -> trackMapper.toTrackMap(
                        track.getId(),
                        track.getIsrc(),
                        trackLikeRepository.existsByMemberAndTrack(member, track),
                        track.getLocation().getLatitude(),
                        track.getLocation().getLongitude()))
                .toList();

        return trackMapper.toTrackMapList(trackMaps);
    }

    @Override
    public TrackResponse.TrackDetail getTrackById(Member member, Long trackId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new RestApiException(TrackErrorCode.TRACK_NOT_FOUND));

        return getTrackDetail(member, track);
    }

    @Transactional
    @Override
    public TrackResponse.TrackId likeTrack(Member member, Long trackId, Boolean isLiked) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new RestApiException(TrackErrorCode.TRACK_NOT_FOUND));

        Optional<TrackLike> existingLike = trackLikeRepository.findByMemberAndTrack(member, track);

        if (isLiked) {
            if (existingLike.isEmpty()) {
                TrackLike trackLike = trackMapper.toTrackLike(member, track);
                trackLikeRepository.save(trackLike);
            }
        } else {
            existingLike.ifPresent(trackLikeRepository::delete);
        }

        return trackMapper.toTrackId(trackId);
    }

    @Transactional
    @Override
    public TrackResponse.TrackId addTrack(Member member, TrackRequest.TrackUpload trackUpload) {
        double latitude = trackUpload.getLatitude();
        double longitude = trackUpload.getLongitude();
        AddressInfo addressInfo = reverseGeoCoding.getAddressInfo(latitude, longitude);

        Location location = locationRepository.save(trackMapper.toLocation(
                addressInfo.buildingName(),
                addressInfo.toAddress(),
                latitude,
                longitude));

        Track track = trackMapper.toTrack(member, location, trackUpload);
        Track savedTrack = trackRepository.save(track);

        return trackMapper.toTrackId(savedTrack.getId());
    }

    @Override
    public TrackResponse.TrackDetailList getTrackFeeds(Member member) {
        List<Track> tracks = trackRepository.findAll(); // TODO: 페이지네이션 또는 친구 트랙만 불러오기

        List<TrackResponse.TrackDetail> trackDetails = tracks.stream()
                .map(track -> getTrackDetail(member, track))
                .toList();

        return trackMapper.toTrackDetailList(trackDetails);
    }

    private TrackDetail getTrackDetail(Member member, Track track) {
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

    @Transactional
    @Override
    public TrackResponse.ReportId reportTrack(Member member, Long trackId) {
        trackRepository.findById(trackId)
                .orElseThrow(() -> new RestApiException(TrackErrorCode.TRACK_NOT_FOUND));

        TrackReport trackReport = trackMapper.toTrackReport(trackId, member.getId());

        TrackReport savedTrackReport = trackReportRepository.save(trackReport);

        return trackMapper.toReportId(savedTrackReport.getId());
    }
}
