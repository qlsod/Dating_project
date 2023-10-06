package com.example.dating.service;

import com.example.dating.dto.AccountDto;
import com.example.dating.domain.Account;
import com.example.dating.repository.AccountRepository;
import com.example.dating.security.jwt.TokenInfo;
import com.example.dating.security.jwt.TokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final ObjectMapper mapper;

    /**
     * 중복 회원이 있는지 체크
     * 검증 후 회원가입
     */
    @Transactional
    public Account join(AccountDto accountDto) {
        Optional<Account> findAccount = accountRepository.findByEmail(accountDto.getEmail());

        if (findAccount.isEmpty()) {
            String encodePassword = passwordEncoder.encode(accountDto.getPassword());

            Account account = Account.builder()
                    .email(accountDto.getEmail())
                    .password(encodePassword)
                    .build();

            accountRepository.save(account);
            return account;
        }
        return null;
    }

    /**
     * 가입된 회원인지 체크
     * 패스워드가 일치하는지 체크
     * 검증 후 TokenInfo 객체를 JSON 으로 변환
     */
    @Transactional
    public ResponseEntity<String> login(AccountDto accountDto) {
        Optional<Account> findAccount = accountRepository.findByEmail(accountDto.getEmail());
        if (findAccount.isEmpty()) {
            return null;
        }

        Account account = findAccount.get();
        if (!passwordEncoder.matches(accountDto.getPassword(), account.getPassword())) {
            return null;
        }

        Authentication token = new UsernamePasswordAuthenticationToken(account.getEmail(), account.getPassword());
        TokenInfo jwt = tokenProvider.generateToken(token);

        return getResponseEntity(jwt);
    }

    private ResponseEntity<String> getResponseEntity(TokenInfo jwt) {
        try {
            String jsonJwt;
            jsonJwt = mapper.writeValueAsString(jwt);
            return ResponseEntity.ok(jsonJwt);
        }
        catch (NullPointerException e) {
            return ResponseEntity.badRequest().body("이메일 혹은 패스워드가 일치하지 않습니다.");
        }
        catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("JSON 변환 중에 오류가 발생했습니다.");
        }
    }
}
