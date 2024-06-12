package com.example.dating.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatOneDto {
//    private Long myId;
//    private Long id;
//    private String image;
    private String nickName;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createAt;

    public ChatOneDto(String nickName, String message, LocalDateTime createAt) {
//        this.id = id;
//        this.image = image;
        this.nickName = nickName;
        this.message = message;
        this.createAt = createAt;
    }

//    public void setImage(String image) {
//        this.image = image;
//    }

//    public void setMyId(Long myId) {
//        this.myId = myId;
//    }
}
