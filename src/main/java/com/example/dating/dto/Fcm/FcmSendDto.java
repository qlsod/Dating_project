package com.example.dating.dto.Fcm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "/fcm RequestDto")
public class FcmSendDto {

    @NotEmpty(message = "상대방의 이름을 입력해주세요")
    private String targetName;

    @NotEmpty(message = "알림 제목을 입력해주세요")
    private String title;

    @NotEmpty(message = "알림 내용을 입력해주세요")
    private String body;

}