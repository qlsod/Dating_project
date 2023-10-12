package com.example.dating.security.jwt;

import com.example.dating.domain.Account;
import com.example.dating.repository.AccountRepository;
import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.security.jwt.refreshtoken.RefreshToken;
import com.example.dating.security.jwt.refreshtoken.RefreshTokenRepository;
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

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    private final Key key;
    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    public TokenProvider(@Value("${jwt.secret}") String secretKey,
                         AccountRepository accountRepository,
                         RefreshTokenRepository refreshTokenRepository) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accountRepository = accountRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public TokenInfo generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + 1000 * 60 * 30);
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

        refreshTokenRepository.save(new RefreshToken(refreshToken, authentication.getName()));

        return TokenInfo.builder()
                .grantType("Bearer ")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = getClaimsJws(accessToken).getBody();
        if (claims.get("auth") == null) {
            return null;
        }
        Account account = accountRepository.findByEmail(claims.getSubject()).get();

        PrincipalDetails principalDetails = new PrincipalDetails(account);
        return new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
    }

    public boolean validateToken(String accessToken) {
        try {
            getClaimsJws(accessToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Jws<Claims> getClaimsJws(String accessToken) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
    }
}
