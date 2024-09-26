package com.example.dating.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    public enum MessageType {
        ENTER, TALK, QUIT
    }

    @NotEmpty
    private Long chatRoomId; // 채팅방 id
    @NotEmpty
    private String nickName; // 보낸 사용자 닉네임
//    private Long memberId; // 보낸 사용자 id
    @NotEmpty
    private MessageType messageType; // 메시지 타입

    @NotEmpty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createAt; // 메세지 입력 시간
    @NotEmpty
    private String message; // 메시지 내용
}
