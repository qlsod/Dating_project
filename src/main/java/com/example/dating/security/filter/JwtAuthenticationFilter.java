package com.example.dating.security.filter;

import com.example.dating.security.jwt.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uri = ((HttpServletRequest) request).getRequestURI();

        // /jwt/refresh에 대해 필터 적용 X
        if ("/jwt/refresh".equals(uri)) {
            chain.doFilter(request, response);
            return;
        }

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
                if (!tokenProvider.validateToken(refreshToken)) {
                    setErrorResponse(httpServletRequest, httpServletResponse, "재로그인 필요");
                } else {
                    setErrorResponse(httpServletRequest, httpServletResponse, "AccessToken 재발급 필요");
                }
            }
        }
        chain.doFilter(request, response);

    }

    public void setErrorResponse(HttpServletRequest req, HttpServletResponse res, String message) throws IOException {

        res.setContentType(MediaType.APPLICATION_JSON_VALUE);

        final Map<String, Object> body = new HashMap<>();

        // 401 반환
        res.setStatus(res.SC_UNAUTHORIZED);
        body.put("status", res.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        // message : 입력한 메시지 반환.
        body.put("message", message);
        body.put("path", req.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(res.getOutputStream(), body);
    }

    // SecurityContext 에 Authentication 객체를 저장
    public void setAuthentication(String token) {
        Authentication authentication = tokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}