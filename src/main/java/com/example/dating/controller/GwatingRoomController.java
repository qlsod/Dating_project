package com.example.dating.controller;

import com.example.dating.dto.gwating.GwatingCardDto;
import com.example.dating.dto.gwating.GwatingRoomDto;
import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.service.GwatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gwating")
@RequiredArgsConstructor
public class GwatingRoomController {

    private final GwatingService gwatingService;

    @GetMapping("/list")
    public ResponseEntity<Object> roomList(@RequestParam String roomCategory) {
        try {
            List<GwatingCardDto> roomList = gwatingService.findRoomList(roomCategory);
            return ResponseEntity.ok(roomList);
        } catch (Exception e) {
            HashMap<String, String> response = new HashMap<>();
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createRoom(@Validated @RequestBody GwatingRoomDto gwatingRoomDto,
                                                          BindingResult bindingResult,
                                                          @AuthenticationPrincipal PrincipalDetails principalDetails) {
        HashMap<String, String> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            response.put("errorMessage", bindingResult.getFieldError().getDefaultMessage());
            return ResponseEntity.badRequest().body(response);
        }

        try {
            gwatingService.create(gwatingRoomDto, principalDetails.getUsername());
            response.put("successMessage", "과팅방 생성 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/join")
    public ResponseEntity<Map<String, String>> joinRoom(@RequestParam Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        HashMap<String, String> response = new HashMap<>();

        try {
            gwatingService.join(id, principalDetails.getUsername());

            // 참여한 회원의 채팅방에 과팅방을 등록하는 로직 추가

            response.put("successMessage", "과팅방 참가 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
