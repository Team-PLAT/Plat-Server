package com.cabin.plat.domain.track.repository;

import com.cabin.plat.domain.track.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {
}
