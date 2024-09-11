package com.cabin.plat.domain.playlist.service;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.playlist.dto.PlaylistRequest;
import com.cabin.plat.domain.playlist.dto.PlaylistResponse;

public interface PlaylistService {
    PlaylistResponse.PlayListId addPlaylist(Member member, PlaylistRequest.PlaylistUpload playlistUpload);
    PlaylistResponse.Playlists getPlaylists(Member member, int page, int size);
    PlaylistResponse.Playlists getSearchedPlaylists(Member member, String title, int page, int size);
    PlaylistResponse.PlaylistDetail getPlaylistDetail(Member member, Long playlistId);
    PlaylistResponse.PlayListId deletePlaylist(Member member, Long playlistId);
}
