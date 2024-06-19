package com.example.dating.dto.Fcm;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmSendDto {
    private Long targetUserId;

    private String title;

    private String body;

    @Builder(toBuilder = true)
    public FcmSendDto(Long targetUserId, String title, String body) {
        this.targetUserId = targetUserId;
        this.title = title;
        this.body = body;
    }
}