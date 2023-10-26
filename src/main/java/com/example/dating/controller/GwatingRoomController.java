package com.example.dating.controller;

import com.example.dating.dto.gwating.GwatingCardDto;
import com.example.dating.dto.gwating.GwatingRoomDto;
import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.service.GwatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GwatingRoomController {

    private final GwatingService gwatingService;

    @GetMapping("/gwating/list")
    public List<GwatingCardDto> roomList(@RequestParam String roomCategory) {
        return gwatingService.findRoomList(roomCategory);
    }

    @PostMapping("/gwating/create")
    public String createRoom(@RequestBody GwatingRoomDto gwatingRoomDto,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (bindingResult.hasErrors()) {
            return bindingResult.getFieldError().getDefaultMessage();
        }

        return gwatingService.create(gwatingRoomDto, principalDetails.getUsername());
    }

    @PostMapping("gwating/join")
    public String joinRoom(@RequestParam Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        gwatingService.join(id, principalDetails.getUsername());

        // 참여한 회원의 채팅방에 과팅방을 등록하는 로직 추가

        return "입장 완료";
    }
}