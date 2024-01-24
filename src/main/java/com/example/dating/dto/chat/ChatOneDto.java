package com.example.dating.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatOneDto {
    private Long myId;
    private Long id;
    private String image;
    private String message;
    private LocalDateTime createAt;

    public ChatOneDto(Long id, String image, String message, LocalDateTime createAt) {
        this.id = id;
        this.image = image;
        this.message = message;
        this.createAt = createAt;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMyId(Long myId) {
        this.myId = myId;
    }
}
