package com.cabin.plat.domain.playlist.service;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.member.repository.MemberRepository;
import com.cabin.plat.domain.playlist.dto.PlaylistRequest;
import com.cabin.plat.domain.playlist.dto.PlaylistRequest.PlaylistOrders;
import com.cabin.plat.domain.playlist.dto.PlaylistRequest.TrackOrder;
import com.cabin.plat.domain.playlist.dto.PlaylistResponse;
import com.cabin.plat.domain.playlist.dto.PlaylistResponse.PlayListId;
import com.cabin.plat.domain.playlist.entity.Playlist;
import com.cabin.plat.domain.playlist.entity.PlaylistTrack;
import com.cabin.plat.domain.playlist.mapper.PlaylistMapper;
import com.cabin.plat.domain.playlist.repository.PlaylistRepository;
import com.cabin.plat.domain.playlist.repository.PlaylistTrackRepository;
import com.cabin.plat.domain.track.dto.TrackResponse;
import com.cabin.plat.domain.track.entity.Track;
import com.cabin.plat.domain.track.repository.TrackRepository;
import com.cabin.plat.domain.track.service.TrackService;
import com.cabin.plat.global.exception.RestApiException;
import com.cabin.plat.global.exception.errorCode.PlaylistErrorCode;
import com.cabin.plat.global.exception.errorCode.TrackErrorCode;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements PlaylistService {

    private final TrackService trackService;
    private final PlaylistRepository playlistRepository;
    private final MemberRepository memberRepository;
    private final TrackRepository trackRepository;
    private final PlaylistTrackRepository playlistTrackRepository;
    private final PlaylistMapper playlistMapper;

    @Transactional
    @Override
    public PlaylistResponse.PlayListId addPlaylist(Member member, PlaylistRequest.PlaylistUpload playlistUpload) {
        Playlist playlist = playlistMapper.toPlaylist(member, playlistUpload);
        playlistRepository.save(playlist);

        List<PlaylistRequest.TrackOrder> trackOrders = playlistUpload.getTracks();
        List<PlaylistTrack> playlistTracks = trackOrders.stream()
                .map(trackOrder -> {
                    Track track = findTrackById(trackOrder.getTrackId());
                    return playlistMapper.toPlaylistTrack(playlist, track, trackOrder.getOrderIndex());
                })
                .toList();

        playlistTrackRepository.saveAll(playlistTracks);

        return playlistMapper.toPlaylistId(playlist.getId());
    }

    @Override
    public PlaylistResponse.Playlists getPlaylists(Member member, int page, int size) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));

        // TODO: 효율 개선
        List<Playlist> playlists = playlistRepository.findAllByMember(member, pageable).getContent();
        List<PlaylistResponse.Playlists.PlaylistInfo> playlistInfos = playlists.stream().map(playlist -> {
            // TODO: playlist.getPlaylistTracks 를 가져오기
            List<PlaylistTrack> playlistTracks = playlistTrackRepository.findAllByPlaylistIs(playlist);
            return playlistMapper.toPlaylistInfo(playlist, playlistTracks);
        }).toList();
        return playlistMapper.toPlaylists(playlistInfos);
    }

    @Override
    public PlaylistResponse.Playlists getSearchedPlaylists(Member member, String title, int page, int size) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));

        List<Playlist> playlists = playlistRepository.findAllByMemberAndTitleContaining(member, title, pageable).getContent();
        List<PlaylistResponse.Playlists.PlaylistInfo> playlistInfos = playlists.stream().map(playlist -> {
            // TODO: playlist.getPlaylistTracks 를 가져오기
            List<PlaylistTrack> playlistTracks = playlistTrackRepository.findAllByPlaylistIs(playlist);
            return playlistMapper.toPlaylistInfo(playlist, playlistTracks);
        }).toList();
        return playlistMapper.toPlaylists(playlistInfos);
    }

    @Transactional
    @Override
    public PlaylistResponse.PlayListId deletePlaylist(Member member, Long playlistId) {
        Playlist playlist = findPlaylistByIdWithValidation(playlistId, member);
        playlist.delete();
        return playlistMapper.toPlaylistId(playlistId);
    }

    @Override
    public PlaylistResponse.PlaylistDetail getPlaylistDetail(Member member, Long playlistId) {
        Playlist playlist = findPlaylistById(playlistId);
        // TODO: playlist.getPlaylistTracks 를 가져오기
        List<PlaylistTrack> playlistTracks = playlistTrackRepository.findAllByPlaylistIs(playlist);
        List<PlaylistResponse.TrackDetailOrder> trackDetailOrders = playlistTracks.stream()
                .map(playlistTrack -> {
                    TrackResponse.TrackDetail trackDetail = trackService.getTrackById(member,
                            playlistTrack.getTrack().getId());
                    return playlistMapper.toTrackDetailOrder(playlistTrack, trackDetail);
                }).toList();
        return playlistMapper.toPlaylistDetail(playlist, trackDetailOrders);
    }

    @Transactional
    @Override
    public PlaylistResponse.PlayListId updatePlaylistTitleAndImage(Member member, Long playlistId, PlaylistRequest.PlaylistEdit playlistEdit) {
        Playlist playlist = findPlaylistByIdWithValidation(playlistId, member);

        // 제목 및 이미지 변경
        playlist.updatePlaylist(playlistEdit.getTitle(), playlistEdit.getPlaylistImageUrl());

        return playlistMapper.toPlaylistId(playlistId);
    }

    @Transactional
    @Override
    public PlaylistResponse.PlayListId addTrackToPlaylist(Member member, Long playlistId, PlaylistRequest.TrackId trackId) {
        Playlist playlist = findPlaylistByIdWithValidation(playlistId, member);
        Track track = findTrackById(trackId.getTrackId());

        // TODO: playlist.getPlaylistTracks 로 가져오기 (현재는 playlistTrackRepository.findAllByPlaylistIs(playlist) 로 대체)
        List<PlaylistTrack> playlistTracks = playlistTrackRepository.findAllByPlaylistIs(playlist);

        if (playlistTracks.stream().anyMatch(playlistTrack -> playlistTrack.getTrack().getId().equals(trackId.getTrackId()))) {
            throw new RestApiException(PlaylistErrorCode.PLAYLIST_TRACK_DUPLICATE);
        }

        int nextOrderIndex = playlistTracks.isEmpty() ? 0 : playlistTracks.stream()
                .mapToInt(PlaylistTrack::getOrderIndex)
                .max()
                .orElse(0) + 1;


        PlaylistTrack playlistTrack = playlistMapper.toPlaylistTrack(playlist, track, nextOrderIndex);
        playlistTrackRepository.save(playlistTrack);
        return playlistMapper.toPlaylistId(playlistId);
    }

    @Transactional
    @Override
    public PlayListId updateTrackOrders(Member member, Long playlistId, PlaylistOrders playlistOrders) {
        Playlist playlist = findPlaylistByIdWithValidation(playlistId, member);

        // 기존 플레이리스트의 모든 트랙
        List<PlaylistTrack> playlistTracks = playlistTrackRepository.findAllByPlaylistIs(playlist);

        // 플레이리스트의 업데이트할 트랙 순서 정보
        Map<Long, Integer> trackOrderMap = playlistOrders.getTracks().stream()
                .collect(Collectors.toMap(TrackOrder::getTrackId, TrackOrder::getOrderIndex));

        validateTrackOrderCount(playlistOrders, playlistTracks);
        validateTrackIds(playlistOrders, playlistTracks);

        for (PlaylistTrack playlistTrack: playlistTracks) {
            Long trackId = playlistTrack.getTrack().getId();
            int newOrder = trackOrderMap.get(trackId);
            playlistTrack.setOrderIndex(newOrder);
        }

        return playlistMapper.toPlaylistId(playlistId);
    }

    @Transactional
    @Override
    public PlayListId deleteTrackFromPlaylist(Member member, Long playlistId, Long trackId) {
        Playlist playlist = findPlaylistByIdWithValidation(playlistId, member);
        Track track = findTrackById(trackId);

        List<PlaylistTrack> playlistTracks = playlistTrackRepository.findAllByPlaylistIs(playlist);
        PlaylistTrack playlistTrack = findPlaylistTrackByTrack(playlistTracks, track);
        playlistTrack.delete();

        int deletedOrderIndex = playlistTrack.getOrderIndex();
        playlistTracks.stream()
                .filter(pt -> pt.getOrderIndex() > deletedOrderIndex)
                .forEach(pt -> pt.setOrderIndex(pt.getOrderIndex() - 1));
        return playlistMapper.toPlaylistId(playlistId);
    }

    private PlaylistTrack findPlaylistTrackByTrack(List<PlaylistTrack> playlistTracks, Track track) {
        return playlistTracks.stream()
                .filter(pt -> pt.getTrack().getId().equals(track.getId()))
                .findFirst()
                .orElseThrow(() -> new RestApiException(PlaylistErrorCode.PLAYLIST_TRACK_NOT_FOUND));
    }

    private void validateTrackOrderCount(PlaylistOrders playlistOrders, List<PlaylistTrack> playlistTracks) {
        if (playlistOrders.getTracks().size() != playlistTracks.size()) {
            throw new RestApiException(PlaylistErrorCode.PLAYLIST_TRACK_COUNT_MISMATCH);
        }
    }

    private void validateTrackIds(PlaylistOrders playlistOrders, List<PlaylistTrack> playlistTracks) {
        Set<Long> playlistTrackIds = playlistTracks.stream()
                .map(playlistTrack -> playlistTrack.getTrack().getId())
                .collect(Collectors.toSet());

        Set<Long> orderTrackIds = playlistOrders.getTracks().stream()
                .map(TrackOrder::getTrackId)
                .collect(Collectors.toSet());

        if (!playlistTrackIds.equals(orderTrackIds)) {
            throw new RestApiException(PlaylistErrorCode.PLAYLIST_TRACK_ID_MISMATCH);
        }
    }

    private Track findTrackById(Long trackId) {
        return trackRepository.findById(trackId)
                .orElseThrow(() -> new RestApiException(TrackErrorCode.TRACK_NOT_FOUND));
    }

    private Playlist findPlaylistById(Long playlistId) {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RestApiException(PlaylistErrorCode.PLAYLIST_NOT_FOUND));
    }

    private Playlist findPlaylistByIdWithValidation(Long playlistId, Member member) {
        Playlist playlist = findPlaylistById(playlistId);
        if (!playlist.getMember().equals(member)) {
            throw new RestApiException(PlaylistErrorCode.PLAYLIST_FORBIDDEN);
        }
        return playlist;
    }
}
