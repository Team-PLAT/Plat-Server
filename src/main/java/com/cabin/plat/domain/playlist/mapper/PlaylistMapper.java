package com.cabin.plat.domain.playlist.mapper;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.playlist.dto.PlaylistRequest.PlaylistUpload;
import com.cabin.plat.domain.playlist.dto.PlaylistResponse;
import com.cabin.plat.domain.playlist.dto.PlaylistResponse.Playlists.PlaylistInfo;
import com.cabin.plat.domain.playlist.dto.PlaylistResponse.TrackDetailOrder;
import com.cabin.plat.domain.playlist.entity.Playlist;
import com.cabin.plat.domain.playlist.entity.PlaylistTrack;
import com.cabin.plat.domain.track.dto.TrackResponse;
import com.cabin.plat.domain.track.entity.Track;
import com.cabin.plat.domain.track.mapper.TrackMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PlaylistMapper {

    private final TrackMapper trackMapper;

    public PlaylistMapper(TrackMapper trackMapper) {
        this.trackMapper = trackMapper;
    }

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

    public PlaylistResponse.Playlists toPlaylists(List<PlaylistInfo> playlistInfos, boolean hasNext) {
        return PlaylistResponse.Playlists.builder()
                .playlists(playlistInfos)
                .hasNext(hasNext)
                .build();
    }

    public PlaylistResponse.Playlists.PlaylistInfo toPlaylistInfo(Playlist playlist,
                                                                  List<PlaylistTrack> playlistTracks) {
        return PlaylistInfo.builder()
                .playlistId(playlist.getId())
                .title(playlist.getTitle())
                .playlistImageUrl(playlist.getPlaylistImageUrl())
                .createdAt(playlist.getCreatedAt())
                .uploaderNicknames(playlistTracks.stream()
                        .map(PlaylistTrack::getTrack)
                        .map(track -> {
                            if (track.getMember() == null) {
                                return "알수없음";
                            }
                            return track.getMember().getNickname();
                        })
                        .collect(Collectors.toUnmodifiableSet()))
                .build();
    }

    public PlaylistResponse.TrackDetailOrder toTrackDetailOrder(PlaylistTrack playlistTrack,
                                                                TrackResponse.TrackDetail trackDetail) {
        return PlaylistResponse.TrackDetailOrder.builder()
                .orderIndex(playlistTrack.getOrderIndex())
                .trackDetail(trackDetail)
                .build();
    }

    public PlaylistResponse.PlaylistDetail toPlaylistDetail(Playlist playlist,
                                                            List<TrackDetailOrder> trackDetailOrders) {
        return PlaylistResponse.PlaylistDetail.builder()
                .playlistId(playlist.getId())
                .title(playlist.getTitle())
                .playlistImageUrl(playlist.getPlaylistImageUrl())
                .createdAt(playlist.getCreatedAt())
                .tracks(trackDetailOrders)
                .build();
    }
}
