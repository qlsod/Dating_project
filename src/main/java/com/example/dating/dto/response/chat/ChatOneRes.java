package com.example.dating.dto.response.chat;

import com.example.dating.dto.chat.ChatOneDto;
import lombok.Data;

import java.util.List;

@Data
public class ChatOneRes {
    private List<ChatOneDto> messages;
}
