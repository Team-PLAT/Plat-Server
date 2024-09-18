package com.cabin.plat.domain.playlist.repository;

import com.cabin.plat.domain.playlist.entity.PlaylistTrack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistTrackRepository extends JpaRepository<PlaylistTrack, Long> {
}
