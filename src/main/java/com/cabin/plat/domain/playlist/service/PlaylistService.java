package com.cabin.plat.domain.playlist.service;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.playlist.dto.PlaylistRequest;
import com.cabin.plat.domain.playlist.dto.PlaylistResponse;

public interface PlaylistService {
    PlaylistResponse.PlayListId addPlaylist(Member member, PlaylistRequest.PlaylistUpload playlistUpload);
    PlaylistResponse.Playlists getPlaylists(Member member, int page, int size);
    PlaylistResponse.Playlists getSearchedPlaylists(Member member, String title, int page, int size);
    PlaylistResponse.PlayListId deletePlaylist(Member member, Long playlistId);
    PlaylistResponse.PlaylistDetail getPlaylistDetail(Member member, Long playlistId);
    PlaylistResponse.PlayListId updatePlaylist(Member member, Long playlistId, PlaylistRequest.PlaylistUpload playlistUpload);
    PlaylistResponse.PlayListId addTrackToPlaylist(Member member, Long playlistId, PlaylistRequest.TrackId trackId);
}
