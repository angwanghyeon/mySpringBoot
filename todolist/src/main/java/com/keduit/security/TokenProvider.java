package com.keduit.security;

import com.keduit.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

//tokenProvider도 service이다
@Service
@Slf4j
public class TokenProvider {

    // 비밀 키는 토큰을 서명하는 데 사용됩니다.
    private static final String SECRET_KEY = "FlRpX30pMqDbiAkmlfArbrmVkDD4RqISskGZmBFax5oGVxzXXWUzTR5JyskiHMIV9M1Oicegkpi46AdvrcX1E6CmTUBc6IFbTPiD";

    // 사용자 엔터티를 받아와서 토큰을 생성하는 메서드입니다.
    public String create(UserEntity userEntity){

        // 현재 시간으로부터 1일 후의 만료 날짜를 계산합니다.
        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        // JSON Web Token을 생성합니다.
        String token = Jwts.builder()
                // 토큰의 서명 알고리즘과 비밀 키를 설정합니다.
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                // 토큰의 주제로 사용자의 ID를 설정합니다.
                .setSubject(userEntity.getId())
                // 토큰의 발급자를 설정합니다.
                .setIssuer("todo app")
                // 토큰의 발급 시간을 설정합니다.
                .setIssuedAt(new Date())
                // 토큰의 만료 시간을 설정합니다.
                .setExpiration(expiryDate)
                // 최종적으로 토큰을 생성합니다.
                .compact();

        // 생성된 토큰을 반환합니다.
        return token;
    }

    //주어진 토큰을 유효성 검사하고, 토큰에 포함된 사용자 ID를 반환하는 메서드입니다.
    public String validateAndGetUserId(String token){

        // 주어진 토큰을 파싱하고, 서명 키를 사용하여 유효성을 검사합니다.
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        // 토큰에서 추출한 사용자 ID를 반환합니다.
        return claims.getSubject();
    }

}






