package com.example.dating.controller;

import com.example.dating.dto.member.MemberCardDto;
import com.example.dating.dto.member.MemberCommonDto;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
            homeRes.setFanMemberList(heartService.receiverHeartList(email));
            homeRes.setFavoriteMemberList(heartService.sendHeartList(email));

            return ResponseEntity.ok(homeRes);
        } catch (Exception e) {
            throw new RuntimeException("메인화면 API 오류 발생");
        }
    }

//    @GetMapping("/sendHeartList")
//    public ResponseEntity<Map<String, Object>> sendHeartList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
//        HashMap<String, Object> response = new HashMap<>();
//
//        try {
//            String email = principalDetails.getUsername();
//            List<MemberCardDto> sendHeartList = memberService.getSendHeartList(email);
//
//            response.put("sendHeartList", sendHeartList);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            response.put("errorMessage", e.getMessage());
//            return ResponseEntity.badRequest().body(response);
//        }
//    }

    @Operation(summary = "나한테 관심 있는 사람 리스트 불러오기 Pagination(20명)",
            description = "나한테 관심 있는 사람 리스트의 마지막 uuid 보내서 다음 리스트 불러오기")
    @GetMapping("/fan-list/{id}")
    public ResponseEntity<List<MemberCommonDto>> receiverHeartList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                                   @PathVariable("id") Long id) {
        try {
            String email = principalDetails.getUsername();

            List<MemberCommonDto> receiverHeartList = heartService.pagingReceiverHeartList(email, id);

            return ResponseEntity.ok(receiverHeartList);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "내가 관심 있는 사람 리스트 불러오기 Pagination(20명)",
            description = "내가 관심 있는 사람 리스트의 마지막 uuid 보내서 다음 리스트 불러오기")
    @GetMapping("/favorite-list/{id}")
    public ResponseEntity<List<MemberCommonDto>> senderHeartList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                                   @PathVariable("id") Long id) {
        try {
            String email = principalDetails.getUsername();

            List<MemberCommonDto> senderHeartList = heartService.pagingSenderHeartList(email, id);

            return ResponseEntity.ok(senderHeartList);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
