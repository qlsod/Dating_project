package com.example.dating.controller;

import com.example.dating.domain.ChatMessage;
import com.example.dating.domain.ChatRead;
import com.example.dating.domain.ChatRoom;
import com.example.dating.dto.chat.ChatListDto;
import com.example.dating.dto.chat.ChatMessageDto;
import com.example.dating.dto.chat.ChatOneDto;
import com.example.dating.dto.response.chat.ChatListRes;
import com.example.dating.dto.response.chat.ChatOneRes;
import com.example.dating.dto.response.chat.ChatRes;
import com.example.dating.exception.ExceptionResponseHandler;
import com.example.dating.repository.ChatReadRepository;
import com.example.dating.repository.ChatRoomRepository;
import com.example.dating.repository.MessageRepository;
import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatRoomService chatRoomService;

    @Operation(summary = "채팅 시작",
            description = "사용자 본인, 타켓 사용자 uid 입력받아 채팅방을 생성하여 채팅방 id를 반환합니다.")
    @SecurityRequirement(name = "accessToken")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "채팅방 id 반환"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @PostMapping("/create/{id}")
    public ResponseEntity<ChatRes> createChatRoom(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                  @PathVariable Long id, @RequestParam(value = "type") String type) {

        try {
            String email = principalDetails.getUsername();

            // 채팅방 존재 여부 확인
            chatRoomService.checkChatRoomExist(email, id);

            Long chatRoomId = chatRoomService.createRoom(email, id, type);
            ChatRes chatRes = new ChatRes(chatRoomId);
            return ResponseEntity.status(HttpStatus.CREATED).body(chatRes);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        // 앱 쪽에서 member와 otherMember가 입장했다는 것을 ws/chat으로 보냄
    }


    @Operation(summary = "본인 채팅 읽음 표시",
            description = "해당 채팅방 id 입력하여 읽음 표시")
    @SecurityRequirement(name = "accessToken")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "읽음 표시 변경 완료"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @PostMapping("/is-read/{id}")
    public ResponseEntity<Void> isRead(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                       @PathVariable Long id) {
        String email = principalDetails.getUsername();
        try {
            chatRoomService.chatIsRead(email, id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "본인 채팅방 목록 조회",
            description = "사용자 본인이 포함된 모든 채팅방 목록을 조회합니다.")
    @SecurityRequirement(name = "accessToken")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 채팅방 목록 반환"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @GetMapping("/list")
    public ResponseEntity<ChatListRes> getChatRoomList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                       @RequestParam String type) {
        String email = principalDetails.getUsername();
        try {
            List<ChatListDto> chatRoomList = chatRoomService.getList(email, type);

            ChatListRes chatListRes = new ChatListRes();
            chatListRes.setChatRoomList(chatRoomList);
            return ResponseEntity.ok(chatListRes);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "채팅방 내용 조회",
            description = "타켓 채팅방의 id를 입력받아 해당 채팅방의 내용을 조회합니다.")
    @SecurityRequirement(name = "accessToken")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채팅방 세부 내용 반환"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @GetMapping("/{roomId}")
    public ResponseEntity<ChatOneRes> getChatRoomOne(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                     @PathVariable Long roomId) {
        HashMap<String, Object> response = new HashMap<>();
        String email = principalDetails.getUsername();

        try {
            List<ChatOneDto> messages = chatRoomService.getOne(email, roomId);

            ChatOneRes chatOneRes = new ChatOneRes();
            chatOneRes.setMessages(messages);
            return ResponseEntity.ok(chatOneRes);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
