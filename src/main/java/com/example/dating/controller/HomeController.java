package com.example.dating.controller;

import com.example.dating.dto.heart.HeartMemberDto;
import com.example.dating.dto.member.MemberCardDto;
import com.example.dating.dto.member.MemberMbtiDto;
import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.service.HeartService;
import com.example.dating.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;
    private final HeartService heartService;

    @GetMapping({"", "/"})
    public HashMap<String, List<?>> home(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 이성 회원 랜덤 20명 추천
        String email = principalDetails.getUsername();
        List<MemberCardDto> randomMemberList = memberService.getRandomMemberList(email);

        // 내가 관심 있는 친구
        List<HeartMemberDto> sendHeartList = heartService.sendHeartList(email);

        // 나한테 관심 있는 친구
        List<HeartMemberDto> receiverHeartList = heartService.receiverHeartList(email);

        // mbti 잘 맞는 이성 회원 랜덤 5명 추천
        List<MemberMbtiDto> goodMbtiList = memberService.getGoodMbtiList(email, randomMemberList);

        HashMap<String, List<?>> response = new HashMap<>();
        response.put("randomMemberList", randomMemberList);
        response.put("sendHeartList", sendHeartList);
        response.put("receiverHeartList", receiverHeartList);
        response.put("goodMbtiList", goodMbtiList);

        return response;
    }
}
