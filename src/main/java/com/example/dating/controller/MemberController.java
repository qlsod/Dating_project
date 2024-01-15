package com.example.dating.controller;

import com.example.dating.dto.email.EmailDto;
import com.example.dating.service.EmailService;
import com.example.dating.redis.service.RedisService;
import com.example.dating.dto.member.MemberJoinDto;
import com.example.dating.dto.member.MemberInfoDto;
import com.example.dating.repository.MemberRepository;
import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.security.jwt.TokenInfo;
import com.example.dating.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final RedisService redisService;
    private final EmailService emailService;
    private final MemberRepository memberRepository;

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

    @PostMapping("/mail/confirm")
    public ResponseEntity<Map<String, String>> mailConfirm(@RequestBody EmailDto emailDto) {
        HashMap<String, String> response = new HashMap<>();
        String email = emailDto.getEmail();
        try {
            // 해당 이메일로 된 계정이 존재하지 않으면
            if (memberRepository.findByEmail(email).isEmpty()) {
                response.put("errorMessage", "일치하는 계정이 없습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            // 존재하면 인증 번호를 메일로 전송
            String code = emailService.sendEmail(email);
            response.put("code", code);
            return ResponseEntity.ok(response);
        } catch (MessagingException | UnsupportedEncodingException e) {
            response.put("errorMessage", "메일 전송 실패");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/password/update")
    public ResponseEntity<Map<String, String>> updatePassword(@RequestBody EmailDto emailDto) {
        HashMap<String, String> response = new HashMap<>();
        try {
            memberService.updatePassword(emailDto);
            response.put("successMessage", "패스워드 변경 성공");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("errorMessage", "패스워드 변경 실패");
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
