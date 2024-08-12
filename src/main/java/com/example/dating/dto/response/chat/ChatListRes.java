package com.example.dating.dto.response.chat;

import com.example.dating.dto.chat.ChatListDto;
import lombok.Data;

import java.util.List;

@Data
public class ChatListRes {

    private List<ChatListDto> chatRoomList;


}
