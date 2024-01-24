package com.example.dating.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatListDto {

    private Long id;
    private String name;
    private String image;
    private String lastMessage;
    private LocalDateTime time;
}
