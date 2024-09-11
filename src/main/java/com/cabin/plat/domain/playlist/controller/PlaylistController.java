package com.cabin.plat.domain.playlist.controller;

import com.cabin.plat.domain.playlist.service.PlaylistService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/playlists")
@Tag(name = "플레이리스트 API", description = "플레이리스트 관련 API 입니다.")
public class PlaylistController {
    private final PlaylistService playlistService;
}
