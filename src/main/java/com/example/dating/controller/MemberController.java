package com.example.dating.controller;

import com.example.dating.redis.service.RedisService;
import com.example.dating.dto.member.MemberJoinDto;
import com.example.dating.dto.member.MemberInfoDto;
import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.security.jwt.TokenInfo;
import com.example.dating.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final RedisService redisService;

    @PostMapping("/join")
    public ResponseEntity<Map<String, String>> join(@Validated @RequestBody MemberJoinDto memberJoinDto, BindingResult bindingResult) {
        HashMap<String, String> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            response.put("errorMessage", bindingResult.getFieldError().getDefaultMessage());
            return ResponseEntity.badRequest().body(response);
        }

        try {
            Long memberId = memberService.join(memberJoinDto);
            response.put("memberId", String.valueOf(memberId));
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/profile/save")
    public ResponseEntity<Map<String, String>> saveMemberProfile(@RequestParam("id") Long memberId,
                                                                 @Validated @RequestBody MemberInfoDto memberInfoDto,
                                                                 BindingResult bindingResult) {
        HashMap<String, String> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            response.put("errorMessage", bindingResult.getFieldError().getDefaultMessage());
            return ResponseEntity.badRequest().body(response);
        }

        try {
            memberService.save(memberId, memberInfoDto);
            response.put("successMessage", "프로필 저장 성공");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Validated @RequestBody MemberJoinDto memberJoinDto, BindingResult bindingResult) {
        HashMap<String, String> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            response.put("errorMessage", bindingResult.getFieldError().getDefaultMessage());
            return ResponseEntity.badRequest().body(response);
        }

        // 로그인 시도
        try {
            TokenInfo jwt = memberService.login(memberJoinDto);
            redisService.setValues(jwt.getRefreshToken(), memberJoinDto.getEmail());
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> getMemberProfile(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        try {
            MemberInfoDto memberProfile = memberService.getMemberProfile(principalDetails.getUsername());
            return ResponseEntity.ok(memberProfile);
        } catch (Exception e) {
            HashMap<String, String> response = new HashMap<>();
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/profile/update")
    public ResponseEntity<Map<String, String>> updateMemberProfile(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                                   @Validated @RequestBody MemberInfoDto memberInfoDto,
                                                                   BindingResult bindingResult) {
        HashMap<String, String> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            response.put("errorMessage", bindingResult.getFieldError().getDefaultMessage());
            return ResponseEntity.badRequest().body(response);
        }

        try {
            memberService.updateMemberProfile(principalDetails.getUsername(), memberInfoDto);
            response.put("successMessage", "프로필 수정 성공");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
