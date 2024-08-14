package com.example.dating.controller;

import com.example.dating.dto.member.MemberCardDto;
import com.example.dating.dto.response.HomeRes;
import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.service.AlertService;
import com.example.dating.service.HeartService;
import com.example.dating.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "메인 화면 API",
            description = "이성 회원 사람 추천 20명, 나한테 관심 있는 사람, 내가 관심 있는 사람 리스트 제공")
    @GetMapping({"", "/"})
    public ResponseEntity<HomeRes> home(@AuthenticationPrincipal PrincipalDetails principalDetails) {
//        HashMap<String, Object> response = new HashMap<>();
        try {
            String email = principalDetails.getUsername();

//            // 회원이 확인하지 않은 알림 개수
//            long notCheckAlert = alertService.countNotCheckAlert(email);


            HomeRes homeRes = new HomeRes();

            homeRes.setRandomMemberList(memberService.getRandomMemberList(email));
            homeRes.setReceiverHeartList(heartService.receiverHeartList(email));
            homeRes.setSendHeartList(heartService.sendHeartList(email));
            // 이성 회원 랜덤 20명 추천
//            List<MemberCardDto> randomMemberList = memberService.getRandomMemberList(email);
////            // mbti 잘 맞는 이성 회원 랜덤 5명 추천
////            List<MemberMbtiDto> goodMbtiList = memberService.getGoodMbtiList(email, randomMemberList);
//            // 내가 관심 있는 친구
//            List<HeartMemberDto> sendHeartList = heartService.sendHeartList(email);
//            // 나한테 관심 있는 친구
//            List<HeartMemberDto> receiverHeartList = heartService.receiverHeartList(email);

//            response.put("randomMemberList", randomMemberList);
////            response.put("goodMbtiList", goodMbtiList);
//            response.put("notCheckAlert", notCheckAlert);
//            response.put("sendHeartList", sendHeartList);
//            response.put("receiverHeartList", receiverHeartList);

            return ResponseEntity.ok(homeRes);
        } catch (Exception e) {
            throw new RuntimeException("메인화면 API 오류 발생");
        }
    }

    @GetMapping("/sendHeartList")
    public ResponseEntity<Map<String, Object>> sendHeartList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        HashMap<String, Object> response = new HashMap<>();

        try {
            String email = principalDetails.getUsername();
            List<MemberCardDto> sendHeartList = memberService.getSendHeartList(email);

            response.put("sendHeartList", sendHeartList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/receiverHeartList")
    public ResponseEntity<Map<String, Object>> receiverHeartList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        HashMap<String, Object> response = new HashMap<>();

        try {
            String email = principalDetails.getUsername();
            List<MemberCardDto> sendHeartList = memberService.getReceiverHeartList(email);

            response.put("sendHeartList", sendHeartList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
