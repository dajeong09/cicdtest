package com.innocamp.dduha.config;

import com.innocamp.dduha.jwt.JwtFilter;
import com.innocamp.dduha.jwt.TokenProvider;
import com.innocamp.dduha.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig
        extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final String SECRET_KEY;
    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

/*
SecurityConfigurerAdapter configure 함수를 override.
UsernamePasswordAuthenticationFilter는 Form based Authentication 방식으로 인증을 진행할 때
아이디, 패스워드 데이터를 파싱하여 인증 요청을 위임하는 필터이다.
 */
    @Override
    public void configure(HttpSecurity httpSecurity) {
        JwtFilter customJwtFilter = new JwtFilter(SECRET_KEY, tokenProvider, userDetailsService);
        httpSecurity.addFilterBefore(customJwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}