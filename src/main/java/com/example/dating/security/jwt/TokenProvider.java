package com.example.dating.security.jwt;

import com.example.dating.redis.service.RedisService;
import com.example.dating.domain.Member;
import com.example.dating.repository.MemberRepository;
import com.example.dating.security.auth.PrincipalDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    private final Key key;
    private final MemberRepository memberRepository;
    private final RedisService redisService;


    public TokenProvider(@Value("${jwt.secret}") String secretKey,
                         MemberRepository memberRepository,
                         RedisService redisService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.memberRepository = memberRepository;
        this.redisService = redisService;
    }

    public TokenInfo generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        Date accessTokenExpiresIn = new Date(now + 1000 * 1000);
        Date refreshTokenExpiresIn = new Date(now + 1000 * 60 * 60 * 24 * 14);

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.builder()
                .grantType("Bearer ")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String createAccessToken(Authentication authentication, HttpServletResponse response) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + 1000 * 60 * 30);

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        this.setHeaderAccessToken(response, accessToken);

        return accessToken;
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = getClaimsJws(accessToken).getBody();
        if (claims.get("auth") == null) {
            return null;
        }
        Member member = memberRepository.findByEmail(claims.getSubject()).get();

        PrincipalDetails principalDetails = new PrincipalDetails(member);
        return new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
    }

    public boolean validateToken(String accessToken) {
        try {
            Jws<Claims> claims = getClaimsJws(accessToken);
            // 엑세스 토큰 만료기한이 현재 날짜 이전이 아니면 통과
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // 엑세스 토큰 헤더 설정
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("Authorization", "Bearer "+ accessToken);
    }

    // 헤더에서 AccessToken 값을 get
    public String resolveAccessToken(HttpServletRequest request) {
        return getToken(request, "Authorization");
    }

    // 헤더에서 RefreshToken 값을 get
    public String resolveRefreshToken(HttpServletRequest request) {
        return getToken(request, "RefreshToken");
    }

    // RefreshToken 으로 email get
    public String getUserEmail(String refreshToken) {
        return redisService.getValues(refreshToken);
    }

    // email 로 password get
    public String getUserPassword(String email) {
        return memberRepository.findByEmail(email).get().getPassword();
    }

    private Jws<Claims> getClaimsJws(String accessToken) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
    }

    private String getToken(HttpServletRequest request, String header) {
        if(request.getHeader(header) != null) {
            return request.getHeader(header).substring(7);
        }
        return null;
    }
}
