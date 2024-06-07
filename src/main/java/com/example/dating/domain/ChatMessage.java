package com.example.dating.domain;

import com.example.dating.dto.chat.ChatMessageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHAT_MESSAGE_ID")
    private Long id;

    private Long chatRoomId; // 채팅방 id

    private String nickName; // 보낸 사용자 id

    private String message; // 메시지 내용

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "created_at")
    private LocalDateTime createdAt; // 보낸 시간

//    @PrePersist
//    protected void onCreate() {
//        createdAt = LocalDateTime.now().withSecond(0).withNano(0);
//    }

    public void mapToEntity(ChatMessageDto chatMessageDto) {
        this.chatRoomId = chatMessageDto.getChatRoomId();
        this.nickName = chatMessageDto.getNickName();
        this.message = chatMessageDto.getMessage();
        this.createdAt = chatMessageDto.getCreateAt();
    }
}
