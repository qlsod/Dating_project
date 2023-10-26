package com.example.dating.service;

import com.example.dating.dto.account.AccountDto;
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

    @Transactional
    public Account join(AccountDto accountDto) {
        // 이메일로 회원 찾기
        Optional<Account> findAccount = accountRepository.findByEmail(accountDto.getEmail());

        if (findAccount.isEmpty()) { // 존재하는 회원이 없다면 회원가입 시작
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

    @Transactional
    public Object login(AccountDto accountDto) throws Exception {
        // 이메일로 회원 찾기
        Optional<Account> findAccount = accountRepository.findByEmail(accountDto.getEmail());
        if (findAccount.isEmpty()) { // 회원이 존재하지 않으면 잘못된 이메일
            throw new Exception("존재하는 회원이 없습니다.");
        }

        Account account = findAccount.get();

        // 복호화한 패스워드와 입력한 패스워드가 다르면 틀린 비밀번호
        if (!passwordEncoder.matches(accountDto.getPassword(), account.getPassword())) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        // jwt 발급
        Authentication token = new UsernamePasswordAuthenticationToken(account.getEmail(), account.getPassword());
        return tokenProvider.generateToken(token);
    }
}
