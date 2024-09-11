package com.cabin.plat.domain.playlist.service;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.playlist.dto.PlaylistRequest.PlaylistUpload;
import com.cabin.plat.domain.playlist.dto.PlaylistResponse.PlayListId;
import com.cabin.plat.domain.playlist.dto.PlaylistResponse.PlaylistDetail;
import com.cabin.plat.domain.playlist.dto.PlaylistResponse.Playlists;
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
    public PlayListId addPlaylist(Member member, PlaylistUpload playlistUpload) {
        return null;
    }

    @Override
    public Playlists getPlaylists(Member member, int page, int size) {
        return null;
    }

    @Override
    public Playlists getSearchedPlaylists(Member member, String title, int page, int size) {
        return null;
    }

    @Override
    public PlaylistDetail getPlaylistDetail(Member member, Long playlistId) {
        return null;
    }

    @Override
    public PlayListId deletePlaylist(Member member, Long playlistId) {
        return null;
    }
}
