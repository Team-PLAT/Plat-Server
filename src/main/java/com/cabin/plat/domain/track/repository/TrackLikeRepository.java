package com.cabin.plat.domain.track.repository;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.track.entity.Track;
import com.cabin.plat.domain.track.entity.TrackLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackLikeRepository extends JpaRepository<TrackLike, Long> {
    boolean existsByMemberAndTrack(Member member, Track track);
    int countByTrack(Track track);
    Optional<TrackLike> findByMemberAndTrack(Member member, Track track);
}
