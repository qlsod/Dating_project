package com.example.dating.dto.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDto {

    public enum MessageType {
        ENTER, TALK, QUIT
    }
    private Long chatRoomId; // 채팅방 id
    private String nickName; // 보낸 사용자 닉네임
//    private Long memberId; // 보낸 사용자 id
    private MessageType messageType; // 메시지 타입
    private String message; // 메시지 내용
}
