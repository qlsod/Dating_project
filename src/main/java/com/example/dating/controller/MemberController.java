package com.example.dating.controller;

import com.example.dating.dto.MemberDto;
import com.example.dating.service.MemberService;
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
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member/info")
    public ResponseEntity<String> info(@Validated @RequestBody MemberDto memberDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        memberService.save(memberDto);
        return new ResponseEntity<>("회원정보 저장 성공", HttpStatus.OK);
    }
}
