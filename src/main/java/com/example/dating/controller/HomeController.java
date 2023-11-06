package com.example.dating.controller;

import com.example.dating.dto.heart.HeartMemberDto;
import com.example.dating.dto.member.MemberCardDto;
import com.example.dating.dto.member.MemberMbtiDto;
import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.service.AlertService;
import com.example.dating.service.HeartService;
import com.example.dating.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;
    private final HeartService heartService;
    private final AlertService alertService;

    @GetMapping({"", "/"})
    public ResponseEntity<Map<String, Object>> home(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        HashMap<String, Object> response = new HashMap<>();

        String email = principalDetails.getUsername();

        // 회원이 확인하지 않은 알림 개수
        long notCheckAlert = alertService.countNotCheckAlert(email);

        try {
            // 이성 회원 랜덤 20명 추천
            List<MemberCardDto> randomMemberList = memberService.getRandomMemberList(email);
            // mbti 잘 맞는 이성 회원 랜덤 5명 추천
            List<MemberMbtiDto> goodMbtiList = memberService.getGoodMbtiList(email, randomMemberList);

            response.put("randomMemberList", randomMemberList);
            response.put("goodMbtiList", goodMbtiList);
        } catch (Exception e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        // 내가 관심 있는 친구
        List<HeartMemberDto> sendHeartList = heartService.sendHeartList(email);

        // 나한테 관심 있는 친구
        List<HeartMemberDto> receiverHeartList = heartService.receiverHeartList(email);

        response.put("notCheckAlert", notCheckAlert);
        response.put("sendHeartList", sendHeartList);
        response.put("receiverHeartList", receiverHeartList);
        return ResponseEntity.ok(response);
    }
}
