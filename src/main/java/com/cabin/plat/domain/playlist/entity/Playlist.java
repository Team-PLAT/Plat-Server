package com.cabin.plat.domain.playlist.entity;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.global.common.BaseEntity;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("deleted_at is null")
public class Playlist extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    private String playlistImageUrl;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL)
    private List<PlaylistTrack> playlistTracks;
}
