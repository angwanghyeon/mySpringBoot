package com.keduit.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig { //Adapter를 extends 받을 필요가 없다.

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.formLogin()
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/");

        http.authorizeRequests()
                .mvcMatchers("/", "/members/**",
                        "/item/**", "/images/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();

        http.exceptionHandling()
                .authenticationEntryPoint(
                        new CustomAuthenticationEntryPoint()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //비밀번호를 암호화 해서 제공
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        //모든 페이지가 시큐리티 필터를 통과하게 되어 있다. 하지만 CSS 같은 애들은 시큐리티와 전혀 무관하기에 그런걸 방지하는...
        return (web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
    }


}
