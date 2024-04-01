package com.example.dating.controller;

import com.example.dating.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/refresh")
@RequiredArgsConstructor
@Slf4j
public class RefreshController {

    private final TokenProvider tokenProvider;
    @PostMapping("")
    public ResponseEntity<Map<String, String>> getRefreshToken(ServletRequest servletRequest, ServletResponse servletResponse) {


        HashMap<String, String> response = new HashMap<>();

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        // refreshToken 꺼내기
        String refreshToken = tokenProvider.resolveRefreshToken(httpServletRequest);

        // refreshToken에 저장된 email 꺼내기
        String email = tokenProvider.getUserEmail(refreshToken);

        String password = tokenProvider.getUserPassword(email);

        //재발급 후, 컨텍스트에 다시 넣기
        Authentication token = new UsernamePasswordAuthenticationToken(email, password);
        String newAccessToken = tokenProvider.createAccessToken(token, httpServletResponse);

        this.setAuthentication(newAccessToken);

        response.put("accessToken", newAccessToken);
        return ResponseEntity.ok(response);
    }


    // SecurityContext 에 Authentication 객체를 저장
    public void setAuthentication(String token) {
        Authentication authentication = tokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}


