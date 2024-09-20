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
import com.cabin.plat.global.exception.errorCode.TrackErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
        return null;
    }

    @Override
    public PlaylistResponse.Playlists getSearchedPlaylists(Member member, String title, int page, int size) {
        return null;
    }

    @Override
    public PlaylistResponse.PlayListId deletePlaylist(Member member, Long playlistId) {
        return null;
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
}
