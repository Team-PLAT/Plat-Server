package com.cabin.plat.domain.playlist.service;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.member.repository.MemberRepository;
import com.cabin.plat.domain.playlist.dto.PlaylistRequest;
import com.cabin.plat.domain.playlist.dto.PlaylistResponse;
import com.cabin.plat.domain.playlist.entity.Playlist;
import com.cabin.plat.domain.playlist.entity.PlaylistTrack;
import com.cabin.plat.domain.playlist.mapper.PlaylistMapper;
import com.cabin.plat.domain.playlist.repository.PlaylistRepository;
import com.cabin.plat.domain.playlist.repository.PlaylistTrackRepository;
import com.cabin.plat.domain.track.entity.Track;
import com.cabin.plat.domain.track.repository.TrackRepository;
import com.cabin.plat.domain.track.service.TrackService;
import com.cabin.plat.global.exception.RestApiException;
import com.cabin.plat.global.exception.errorCode.PlaylistErrorCode;
import com.cabin.plat.global.exception.errorCode.TrackErrorCode;
import java.util.*;
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

        List<PlaylistRequest.PlaylistUpload.TrackOrder> trackOrders = playlistUpload.getTracks();
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
            List<PlaylistTrack> playlistTracks = playlistTrackRepository.findAllByPlaylistIs(playlist);
            return playlistMapper.toPlaylistInfo(playlist, playlistTracks);
        }).toList();
        return playlistMapper.toPlaylists(playlistInfos);
    }

    @Override
    public PlaylistResponse.PlayListId deletePlaylist(Member member, Long playlistId) {
        Playlist playlist = findPlaylistById(playlistId);
        if (!playlist.getMember().equals(member)) {
            throw new RestApiException(PlaylistErrorCode.PLAYLIST_DELETE_FORBIDDEN);
        }
        playlist.delete();
        return playlistMapper.toPlaylistId(playlistId);
    }

    @Override
    public PlaylistResponse.PlaylistDetail getPlaylistDetail(Member member, Long playlistId) {
        return null;
    }

    @Override
    public PlaylistResponse.PlayListId updatePlaylist(Member member, Long playlistId, PlaylistRequest.PlaylistUpload playlistUpload) {
        return null;
    }

    @Override
    public PlaylistResponse.PlayListId addTrackToPlaylist(Member member, Long playlistId, PlaylistRequest.TrackId trackId) {
        return null;
    }

    private Track findTrackById(Long trackId) {
        return trackRepository.findById(trackId)
                .orElseThrow(() -> new RestApiException(TrackErrorCode.TRACK_NOT_FOUND));
    }

    private Playlist findPlaylistById(Long playlistId) {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RestApiException(PlaylistErrorCode.PLAYLIST_NOT_FOUND));
    }
}
