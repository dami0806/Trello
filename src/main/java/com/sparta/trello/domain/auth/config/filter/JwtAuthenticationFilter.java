package com.sparta.trello.domain.auth.config.filter;


import com.sparta.trello.domain.auth.entity.Auth;
import com.sparta.trello.domain.auth.repository.AuthRepository;
import com.sparta.trello.domain.auth.service.UserDetailsServiceImpl;
import com.sparta.trello.domain.auth.util.JwtUtil;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.domain.user.entity.UserStatus;
import com.sparta.trello.domain.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * Jwt 인증필터: 모든 HTTP가 거침
 * 클라이언트로 부터 HTTP요청을 가로채서 JWT토큰 검사하고
 * 유효한 토큰이면 사용자 정보를 SecurityContext에 저장
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    /**
     * 요청 필터링: JWT 토큰을 검증해서 유효하면 사용자 정보 설정
     *
     * @param request  HTTP 요청
     * @param response HTTP 응답
     * @param chain    필터 체인
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 요청헤더에서 Authorization 추출
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // authorizationHeader가 존재하고, "Bearer "로 시작할 경우
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // "Bearer " 이후의 JWT 토큰 부분
            username = jwtUtil.getUsernameFromToken(jwt); // JWT 토큰에서 사용자 이름 추출
        }

        // 사용자 이름이 존재하고, 현재 SecurityContext에 인증 정보가 없는 경우
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            Optional<Auth> optionalAuth = authRepository.findByName(username);
            if (optionalAuth.isPresent()) {
                Auth auth = optionalAuth.get();
                User user = auth.getUser();
                String refreshToken = user.getRefreshToken();

                if (user.getUserStatus().equals(UserStatus.WITHDRAWN)) {
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("탈퇴한 회원입니다.");
                    return;
                }

                if (refreshToken == null) {
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("다시 로그인하세요");
                    return;
                }

                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        chain.doFilter(request, response);
    }
}