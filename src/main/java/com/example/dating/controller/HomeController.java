package com.example.dating.controller;

import com.example.dating.dto.heart.HeartMemberDto;
import com.example.dating.dto.member.MemberCardDto;
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
        // 회원 카드
        String email = principalDetails.getUsername();
        List<MemberCardDto> randomMemberList = memberService.getRandomMemberList(email);

        // 내가 관심 있는 친구
        List<HeartMemberDto> sendHeartList = heartService.sendHeartList(email);

        // 나한테 관심 있는 친구
        List<HeartMemberDto> receiverHeartList = heartService.receiverHeartList(email);

        HashMap<String, List<?>> response = new HashMap<>();
        response.put("randomMemberList", randomMemberList);
        response.put("sendHeartList", sendHeartList);
        response.put("receiverHeartList", receiverHeartList);

        return response;
    }
}
