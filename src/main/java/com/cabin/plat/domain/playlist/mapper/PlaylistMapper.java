package com.cabin.plat.domain.playlist.mapper;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.playlist.dto.PlaylistRequest.PlaylistUpload;
import com.cabin.plat.domain.playlist.dto.PlaylistResponse;
import com.cabin.plat.domain.playlist.entity.Playlist;
import com.cabin.plat.domain.playlist.entity.PlaylistTrack;
import com.cabin.plat.domain.track.entity.Track;
import org.springframework.stereotype.Component;

@Component
public class PlaylistMapper {

    public Playlist toPlaylist(Member member, PlaylistUpload playlistUpload) {
        return Playlist.builder()
                .member(member)
                .title(playlistUpload.getTitle())
                .playlistImageUrl(playlistUpload.getPlaylistImageUrl())
                .build();
    }

    public PlaylistTrack toPlaylistTrack(Playlist playlist, Track track, int orderIndex) {
        return PlaylistTrack.builder()
                .playlist(playlist)
                .track(track)
                .orderIndex(orderIndex)
                .build();
    }

    public PlaylistResponse.PlayListId toPlaylistId(Long playlistId) {
        return PlaylistResponse.PlayListId.builder()
                .playlistId(playlistId)
                .build();
    }
}
