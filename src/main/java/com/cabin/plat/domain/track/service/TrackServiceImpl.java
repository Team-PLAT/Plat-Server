package com.cabin.plat.domain.track.service;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.track.dto.TrackRequest;
import com.cabin.plat.domain.track.dto.TrackResponse;
import com.cabin.plat.domain.track.dto.TrackResponse.TrackDetail;
import com.cabin.plat.domain.track.dto.TrackResponse.TrackId;
import com.cabin.plat.domain.track.entity.*;
import com.cabin.plat.domain.track.mapper.TrackMapper;
import com.cabin.plat.domain.track.repository.*;
import com.cabin.plat.global.exception.RestApiException;
import com.cabin.plat.global.exception.errorCode.TrackErrorCode;
import com.cabin.plat.global.util.geocoding.AddressInfo;
import com.cabin.plat.global.util.geocoding.ReverseGeoCoding;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        Track track = findTrackById(trackId);

        return getTrackDetail(member, track);
    }

    @Transactional
    @Override
    public TrackResponse.TrackId likeTrack(Member member, Long trackId, Boolean isLiked) {
        Track track = findTrackById(trackId);

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
    public TrackResponse.TrackDetailList getTrackFeeds(Member member, int page, int size) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));

        List<Track> tracks = trackRepository.findAll(pageable).getContent();

        List<TrackResponse.TrackDetail> trackDetails = tracks.stream()
                .map(track -> getTrackDetail(member, track))
                .toList();

        return trackMapper.toTrackDetailList(trackDetails);
    }

    @Override
    @Transactional
    public TrackId deleteTrack(Member member, Long trackId) {
        Track track = findTrackById(trackId);
        Long trackUploaderId = track.getMember().getId();
        Long memberId = member.getId();
        if (!trackUploaderId.equals(memberId)) {
            throw new RestApiException(TrackErrorCode.TRACK_DELETE_FORBIDDEN);
        }
        track.delete();
        return trackMapper.toTrackId(track.getId());
    }

    @Transactional
    @Override
    public TrackResponse.ReportId reportTrack(Member member, Long trackId) {
        findTrackById(trackId);

        TrackReport trackReport = trackMapper.toTrackReport(trackId, member.getId());

        TrackReport savedTrackReport = trackReportRepository.save(trackReport);

        return trackMapper.toReportId(savedTrackReport.getId());
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
                track.getLocation().getBuildingName(),
                track.getLocation().getAddress(),
                track.getImageUrl(),
                track.getContent(),
                trackLikeRepository.countByTrack(track),
                trackLikeRepository.existsByMemberAndTrack(member, track),
                memberInfo
        );
    }

    private Track findTrackById(Long trackId) {
        return trackRepository.findById(trackId)
                .orElseThrow(() -> new RestApiException(TrackErrorCode.TRACK_NOT_FOUND));
    }
}
