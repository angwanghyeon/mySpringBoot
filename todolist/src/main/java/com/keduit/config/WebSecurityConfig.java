package com.keduit.config;

import com.keduit.security.JwtAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Spring Security를 설정하는 클래스입니다.
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Cross-Origin Resource Sharing (CORS) 설정을 활성화하고, CSRF 공격 방지를 비활성화합니다.
        http.cors()
                .and()
                .csrf().disable()
                .httpBasic().disable()

                // 세션 관리 방식을 STATELESS로 설정하여 세션을 사용하지 않음을 명시합니다.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // /와 /auth/** 경로에 대한 요청은 인증 없이 허용됩니다.
                .authorizeRequests().antMatchers("/", "/auth/**").permitAll()

                // 그 외의 모든 요청은 인증을 필요로 합니다.
                .anyRequest().authenticated();

        // JWT 인증 필터를 등록하고, CORS 필터 이후에 실행되도록 설정합니다.
        http.addFilterAfter(
                jwtAuthenticationFilter, CorsFilter.class
        );
    }
}

