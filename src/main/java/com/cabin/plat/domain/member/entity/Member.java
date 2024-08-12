package com.cabin.plat.domain.member.entity;

import com.cabin.plat.global.common.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
//@SQLRestriction("deleted_at is null")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PermissionRole permissionRole;

    // Apple Social Login시 반환되는 userIdentifier을 SHA256 방식으로 암호화 시킨 String 값.
    // 유저 중복 검사할 때 사용.
    @Column(nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column
    @Setter
    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column
    @Setter
    private StreamType streamType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialType socialType;
}