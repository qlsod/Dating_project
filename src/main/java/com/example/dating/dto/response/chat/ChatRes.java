package com.example.dating.dto.response.chat;

import lombok.Data;

@Data
public class ChatRes {
    private Long chatRoomId;
    public ChatRes(Long chatRoomId){
        this.chatRoomId = chatRoomId;
    }
}
