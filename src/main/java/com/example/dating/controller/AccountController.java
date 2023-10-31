package com.example.dating.controller;

import com.example.dating.domain.Account;
import com.example.dating.dto.account.AccountDto;
import com.example.dating.security.jwt.TokenInfo;
import com.example.dating.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/member/join")
    public ResponseEntity<String> join(@Validated @RequestBody AccountDto accountDto, BindingResult bindingResult) {
        // 필드 에러 검증
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }
        // 회원가입 시도
        Account joinAccount = accountService.join(accountDto);

        if (joinAccount == null) { // 이미 가입된 이메일이라면
            return ResponseEntity.badRequest().body("이미 가입된 이메일입니다.");
        }
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/member/login")
    public ResponseEntity<Object> login(@Validated @RequestBody AccountDto accountDto, BindingResult bindingResult) {
        // 필드 에러 검증
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }

        // 로그인 시도
        try {
            TokenInfo jwt = (TokenInfo) accountService.login(accountDto);
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
