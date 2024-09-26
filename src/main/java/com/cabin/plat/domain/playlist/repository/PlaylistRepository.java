package com.cabin.plat.domain.playlist.repository;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.domain.playlist.entity.Playlist;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    Page<Playlist> findAllByMember(Member member, Pageable pageable);
    Page<Playlist> findAllByMemberAndTitleContainingIgnoreCase(Member member, String title, Pageable pageable);
}
