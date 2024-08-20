package com.cabin.plat.domain.track.entity;

import com.cabin.plat.domain.member.entity.Member;
import com.cabin.plat.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TrackReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_report_id")
    private Long id;

    private String reportTrackId;

    private String reportMemberId;
}