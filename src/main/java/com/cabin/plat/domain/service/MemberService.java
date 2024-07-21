package com.cabin.plat.domain.service;

import com.cabin.plat.domain.entity.Member;
import com.cabin.plat.domain.entity.StreamAccountType;

public interface MemberService {
    Member getMemberById(Long id);
    StreamAccountType getStreamAccountTypeById(Long id);
    void signOut(Long id, String token);
    Member updateAvatar(Long id, String avatar, String token);
    Member updateNickname(Long id, String newNickname, String token);
    Member updateStreamAccountType(Long id, StreamAccountType streamAccountType, String token);
    Long extractUserIdFromToken(String token);
}
