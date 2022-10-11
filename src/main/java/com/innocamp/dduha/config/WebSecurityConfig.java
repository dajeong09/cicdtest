package com.innocamp.dduha.config;

import com.innocamp.dduha.jwt.AccessDeniedHandlerException;
import com.innocamp.dduha.jwt.AuthenticationEntryPointException;
import com.innocamp.dduha.jwt.TokenProvider;
import com.innocamp.dduha.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Value("${jwt.secret}")
    String SECRET_KEY;
    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationEntryPointException authenticationEntryPointException;
    private final AccessDeniedHandlerException accessDeniedHandlerException;

    // BCryptPasswordEncoder는 비밀번호를 암호화하는 데 사용할 수 있는 메서드를 가진 클래스이다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors();

        http.csrf().disable()

                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointException)
                .accessDeniedHandler(accessDeniedHandlerException)


                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/member/**").permitAll()
                .antMatchers("/oauth/**").permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/touristspot/**").permitAll()
                .antMatchers("/restaurant/**").permitAll()
                .antMatchers("/accommodation/**").permitAll()
                .antMatchers("/trip/**").permitAll()
                .antMatchers("/course/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .apply(new JwtSecurityConfig(SECRET_KEY, tokenProvider, userDetailsService));

        return http.build();
    }
}
