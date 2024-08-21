package com.cabin.plat.config.jwt.filter;

import com.cabin.plat.config.jwt.service.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        토큰 추출
        String authorization = request.getHeader("Authorization");
//        String refreshToken = request.getHeader("Refresh-Token");
//        토큰 유형 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

//        토큰 분리
        String token = authorization.split(" ")[1];

//        토큰 만료 여부 확인
        if (jwtUtil.isExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }

//        토큰에서 tokenType 추출
        String tokenType = jwtUtil.getTokenType(token);
        if (tokenType == null) {
            filterChain.doFilter(request, response);
            return;
        }
        // JWT 토큰에서 인증 정보를 추출해 Authentication(인증) 객체를 생성
        Authentication authentication = jwtUtil.getAuthentication(token);
        // Spring Security의 SecurityContextHolder에 설정해 인증된 사용자로 만듦
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}
