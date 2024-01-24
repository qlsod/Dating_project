package com.example.dating.domain;

import com.example.dating.dto.chat.ChatMessageDto;
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

    private Long memberId; // 보낸 사용자 id

    private String message; // 메시지 내용

    @Column(name = "created_at")
    private LocalDateTime createdAt; // 보낸 시간

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now().withSecond(0).withNano(0);
    }

    public void mapToEntity(ChatMessageDto chatMessageDto) {
        this.chatRoomId = chatMessageDto.getChatRoomId();
        this.memberId = chatMessageDto.getMemberId();
        this.message = chatMessageDto.getMessage();
    }
}
