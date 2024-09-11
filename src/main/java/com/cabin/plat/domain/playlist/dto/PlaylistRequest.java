package com.cabin.plat.domain.playlist.dto;

import java.util.List;
import lombok.*;

public class PlaylistRequest {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlaylistUpload {
        private String title;
        private String playlistImageUrl;
        private List<Long> trackIds;
    }
}
