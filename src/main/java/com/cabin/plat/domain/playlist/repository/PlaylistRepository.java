package com.cabin.plat.domain.playlist.repository;

import com.cabin.plat.domain.playlist.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}
