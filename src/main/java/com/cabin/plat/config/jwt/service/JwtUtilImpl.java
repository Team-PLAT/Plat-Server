package com.cabin.plat.config.jwt.service;

import com.cabin.plat.config.jwt.dto.JwtProperties;
import com.cabin.plat.config.security.CustomUserDetails;
import com.cabin.plat.config.security.service.JpaUserDetailService;
import com.cabin.plat.domain.member.dto.MemberResponse;
import com.cabin.plat.domain.member.entity.PermissionRole;
import com.cabin.plat.domain.member.entity.RefreshToken;
import com.cabin.plat.domain.member.mapper.AuthenticationMapper;
import com.cabin.plat.domain.member.repository.RefreshTokenRepository;
import com.cabin.plat.global.exception.RestApiException;
import com.cabin.plat.global.exception.errorCode.AuthErrorCode;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtilImpl implements JwtUtil {
    private SecretKey secretKey;
    private final JwtProperties jwtProperties;
    private final AuthenticationMapper authenticationMapper;
    private final JpaUserDetailService userDetailService;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostConstruct
    protected void init() {
        secretKey = new SecretKeySpec(jwtProperties.getJwt_secret().getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS512.key().build().getAlgorithm());
    }


    // 엑세스 토큰 발급
    @Override
    public String createAccessToken(Long memberId, String clientId, PermissionRole permissionRole) {
        Long expiredTime = jwtProperties.getAccess_expired_time();
        if (expiredTime == null) {
            throw new RestApiException(AuthErrorCode.INVALID_TOKEN_TYPE);
        }
        return Jwts.builder()
                .claim("tokenType", "access")
                .claim("memberId", memberId)
                .claim("clientId", clientId)
                .claim("permissionRole", permissionRole)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredTime))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    // 리프래쉬 토큰 발급
    @Override
    public String createRefreshToken() {
        Claims claims = Jwts.claims().build();
        Long expiredTime = jwtProperties.getRefresh_expired_time();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTime))//유효시간 (3일)
                .signWith(SignatureAlgorithm.HS256, secretKey) //HS256알고리즘으로 key를 암호화 해줄것이다.
                .compact();
    }

    // 토큰 유효성 검사
    @Override
    public Boolean checkJwt(String token) { //TODO 토큰 유효성 검증 로직 수정 필요
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new RestApiException(AuthErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new RestApiException(AuthErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new RestApiException(AuthErrorCode.UNSUPPORTED_TOKEN);
        } catch (SignatureException e) {
            throw new RestApiException(AuthErrorCode.WRONG_TOKEN_SIGNITURE);
        } catch (IllegalArgumentException e) {
            throw new RestApiException(AuthErrorCode.EMPTY_TOKEN);
        }
    }

    // 토큰 두 개 모두 재발급
    @Override
    public MemberResponse.MemberTokens refreshTokens(Long memberId, String clientId, PermissionRole permissionRole) {
        String accessToken = createAccessToken(memberId, clientId, permissionRole);
        String refreshToken = createRefreshToken();

        // 레디스에 refreshToken을 저장. (사용자 기본키 ID, refreshToken, accessToken 저장)
        refreshTokenRepository.save(new RefreshToken(memberId, refreshToken, accessToken));
        return authenticationMapper.toMemberTokens(accessToken, refreshToken);
    }

    @Override
    public Authentication getAuthentication(String token) {
        CustomUserDetails userDetails = userDetailService.loadUserByUsername(Long.toString(getMemberId(token)));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public Long getMemberId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("memberId", Long.class);
    }

    @Override
    public String getClientId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("clientId", String.class);
    }

    @Override
    public String getPermissionRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("permissionRole", String.class);
    }

    @Override
    public String getTokenType(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("tokenType", String.class);
    }

    @Override
    public Boolean isExpired(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException e) {
            throw new RestApiException(AuthErrorCode.INVALID_TOKEN); // 유효하지 않은 경우
        }
    }

}
