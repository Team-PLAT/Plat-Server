package com.cabin.plat.domain.playlist.service;

import com.cabin.plat.domain.playlist.mapper.PlaylistMapper;
import com.cabin.plat.domain.playlist.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final PlaylistMapper playlistMapper;
}
