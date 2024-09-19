package com.example.dating.domain;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class ChatRead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_read_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    private Long userId;

    private Boolean isRead;

    public ChatRead(ChatRoom chatRoom, Long userId, Boolean isRead) {
        this.chatRoom = chatRoom;
        this.userId = userId;
        this.isRead = isRead;
    }
}
