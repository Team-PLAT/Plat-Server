package com.cabin.plat.domain.track.repository;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.track.entity.Track;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TrackRepository extends JpaRepository<Track, Long> {
    @Query(value =
            "SELECT t.* " +
            "FROM track t " +
            "JOIN location l ON t.location_id = l.location_id " +
            "WHERE l.latitude BETWEEN :minLatitude AND :maxLatitude " +
            "AND l.longitude BETWEEN :minLongitude AND :maxLongitude",
            nativeQuery = true)
    List<Track> findAllTracksWithinBounds(
            @Param("minLatitude") double minLatitude,
            @Param("maxLatitude") double maxLatitude,
            @Param("minLongitude") double minLongitude,
            @Param("maxLongitude") double maxLongitude);
    Page<Track> findAll(Pageable pageable);
    List<Track> findAllByMember(Member member);
}