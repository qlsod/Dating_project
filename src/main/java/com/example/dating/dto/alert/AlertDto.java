package com.example.dating.dto.alert;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlertDto {
    private String name;
    private String image;
    private String message;
    private String sendAt;
    private boolean isCheck;
}
