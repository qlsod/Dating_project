package com.example.dating.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createAt; // 메세지 입력 시간
    private String message; // 메시지 내용
}
