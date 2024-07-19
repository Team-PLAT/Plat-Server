package com.cabin.plat.domain.repository;

import com.cabin.plat.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
