package com.example.dating.security.config;

import com.example.dating.security.filter.JwtAuthenticationFilter;
import com.example.dating.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .cors().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().frameOptions().disable() // X-Frame-Options 비활성화
                .and()
                .authorizeRequests(authorize ->
                        authorize
                                .antMatchers("/member/join").permitAll()
                                .antMatchers("/member/login").permitAll()
                                .antMatchers("/member/profile/save").permitAll()
                                .antMatchers("/member/mail/confirm").permitAll()
                                .antMatchers("/refresh").permitAll()
                                .antMatchers("/ws/**").permitAll()
                                .antMatchers(HttpMethod.GET, "/search/**").permitAll() // GET 요청 허용
                                .antMatchers("/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**", "/swagger-resources",
                                        "/swagger-resources/**", "/configuration/ui", "/configuration/security", "/swagger-ui/**",
                                        "/webjars/**", "/swagger-ui.html").permitAll()
                                .antMatchers("/h2-console/**").permitAll()
                                .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
