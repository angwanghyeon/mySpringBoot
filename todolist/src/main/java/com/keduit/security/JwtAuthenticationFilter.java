package com.keduit.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;

    /**
     * HTTP 요청에 대한 JWT 인증 필터를 정의한 클래스입니다.
     * OncePerRequestFilter를 상속하여 모든 요청에 대해 한 번만 실행되도록 보장합니다.
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // HTTP 요청에서 Bearer 토큰을 추출합니다.
            String token = parseBearerToken(request);
            log.info("Filter is running.....");

            // 토큰이 존재할 경우, 유효성을 검사하고 사용자 ID를 추출합니다.
            if (token != null){
                String userId = tokenProvider.validateAndGetUserId(token);
                log.info("userId....." + userId);

                // 추출한 사용자 ID로 인증 토큰을 생성합니다.
                AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userId,
                                null,
                                AuthorityUtils.NO_AUTHORITIES
                        );

                // 인증 토큰에 현재 요청의 세부 정보를 추가합니다.
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 보안 컨텍스트에 인증 토큰을 설정합니다.
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(securityContext);
            }
        } catch (Exception e) {
            // 사용자 인증 컨텍스트 생성 중 에러가 발생한 경우 로깅합니다.
            logger.error("사용자 인증 컨텍스트 생성 에러:", e);
        }

        // 다음 필터로 요청을 전달합니다.
        filterChain.doFilter(request, response);
    }

    /**
     * HTTP 요청에서 Bearer 토큰을 추출하는 메서드입니다.
     *
     * @param request HTTP 요청 객체
     * @return 추출된 Bearer 토큰 (토큰이 없을 경우 null 반환)
     */
    private String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // Authorization 헤더에서 Bearer 토큰을 추출합니다.
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }

        // 헤더에 Bearer 토큰이 없는 경우 null 반환합니다.
        return null;
    }
}

