package com.example.dating.controller;

import com.example.dating.dto.chat.ChatListDto;
import com.example.dating.dto.chat.ChatOneDto;
import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/create/{id}")
    public ResponseEntity<Map<String, String>> createChatRoom(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                              @PathVariable Long id) {
        HashMap<String, String> response = new HashMap<>();

        try {
            String email = principalDetails.getUsername();
            Long chatRoomId = chatRoomService.createRoom(email, id);
            response.put("chatRoomId", chatRoomId.toString());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        // 앱 쪽에서 member와 otherMember가 입장했다는 것을 ws/chat으로 보냄
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getChatRoomList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                               @RequestParam String type) {
        HashMap<String, Object> response = new HashMap<>();
        String email = principalDetails.getUsername();
        try {
            List<ChatListDto> chatRoomList = chatRoomService.getList(email, type);
            response.put("chatRoomList", chatRoomList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("aaa");
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Map<String, Object>> getChatRoomOne(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                              @PathVariable Long roomId) {
        HashMap<String, Object> response = new HashMap<>();
        String email = principalDetails.getUsername();

        try {
            List<ChatOneDto> chatOneDtoList = chatRoomService.getOne(email, roomId);
            response.put("chatOneDtoList", chatOneDtoList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
