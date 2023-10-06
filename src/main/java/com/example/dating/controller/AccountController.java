package com.example.dating.controller;

import com.example.dating.dto.AccountDto;
import com.example.dating.service.AccountService;
import lombok.RequiredArgsConstructor;
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
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }

        if (accountService.join(accountDto) != null) {
            return ResponseEntity.ok("회원가입 성공");
        }
        return ResponseEntity.badRequest().body("이미 가입된 이메일입니다.");
    }

    @PostMapping("/member/login")
    public ResponseEntity<String> login(@Validated @RequestBody AccountDto accountDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }

        return accountService.login(accountDto);
    }
}
