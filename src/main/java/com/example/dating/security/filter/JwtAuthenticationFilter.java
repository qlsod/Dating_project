package com.example.dating.security.filter;

import com.example.dating.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // 헤더에서 JWT 를 get
        String accessToken = tokenProvider.resolveAccessToken(httpServletRequest);
        String refreshToken = tokenProvider.resolveRefreshToken(httpServletRequest);

        // 유효한 토큰인지 확인
        if (accessToken != null) {
            if (tokenProvider.validateToken(accessToken)) {
                this.setAuthentication(accessToken);
            }
            // AccessToken 이 만료되고 RefreshToken 은 존재하면
            else if (!tokenProvider.validateToken(accessToken) && refreshToken != null) {
                String email = tokenProvider.getUserEmail(refreshToken);
                // RefreshToken 이 만료되지 않았다면 (만료되지 않으면 email 값을 가져오고, 만료되면 null 값을 가져옴)
                if (email != null) {
                    //재발급 후, 컨텍스트에 다시 넣기
                    String password = tokenProvider.getUserPassword(email);
                    Authentication token = new UsernamePasswordAuthenticationToken(email, password);
                    String newAccessToken = tokenProvider.createAccessToken(token, httpServletResponse);
                    this.setAuthentication(newAccessToken);
                }
            }
        }
        chain.doFilter(request, response);
    }

    // SecurityContext 에 Authentication 객체를 저장
    public void setAuthentication(String token) {
        Authentication authentication = tokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}