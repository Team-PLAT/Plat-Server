package com.cabin.plat.domain.playlist.service;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.playlist.dto.PlaylistRequest;
import com.cabin.plat.domain.playlist.dto.PlaylistRequest.TrackId;
import com.cabin.plat.domain.playlist.dto.PlaylistResponse;
import com.cabin.plat.domain.playlist.dto.PlaylistResponse.PlayListId;
import com.cabin.plat.domain.playlist.mapper.PlaylistMapper;
import com.cabin.plat.domain.playlist.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final PlaylistMapper playlistMapper;

    @Override
    public PlaylistResponse.PlayListId addPlaylist(Member member, PlaylistRequest.PlaylistUpload playlistUpload) {
        return null;
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
}
