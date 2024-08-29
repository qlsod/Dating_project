package com.example.dating.dto.alert;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AlertDto {
    private String image;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "created_at")
    private LocalDateTime createdAt; // 보낸 시간
    private boolean chatExist;
}
