package com.cabin.plat.config.jwt.service;

import com.cabin.plat.domain.member.dto.MemberResponse;
import com.cabin.plat.domain.member.entity.PermissionRole;
import org.springframework.security.core.Authentication;

public interface JwtUtil {
    String createAccessToken(Long memberId, String clientId, PermissionRole permissionRole);
    //    String createJwtFromEncryptedUserIdentifier(String encryptedUserIdentifier);
    Boolean checkJwt(String token);
    MemberResponse.MemberTokens refreshTokens(Long memberId, String clientId, PermissionRole permissionRole);
    Authentication getAuthentication(String token);
    Long getMemberId(String token);
    String getClientId(String token);
    String getPermissionRole(String token);
    String getTokenType(String token);
    Boolean isExpired(String token);
    String createRefreshToken();
}
