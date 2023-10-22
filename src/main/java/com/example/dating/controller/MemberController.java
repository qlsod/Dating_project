package com.example.dating.controller;

import com.example.dating.dto.member.MemberInfoDto;
import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member/profile/save")
    public String saveMemberProfile(
            @Validated @RequestBody MemberInfoDto memberInfoDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        if (bindingResult.hasErrors()) {
            return "회원정보 저장 실패";
        }

        return memberService.save(principalDetails.getUsername(), memberInfoDto);
    }

    @GetMapping("/member/profile")
    public MemberInfoDto getMemberProfile(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return memberService.getMemberProfile(principalDetails.getUsername());
    }

    @PostMapping("/member/profile/update")
    public String updateMemberProfile(@RequestBody MemberInfoDto memberInfoDto,
                                      BindingResult bindingResult,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (bindingResult.hasErrors()) {
            return "회원정보 수정 실패";
        }
        return memberService.updateMemberProfile(principalDetails.getUsername(), memberInfoDto);
    }
}
