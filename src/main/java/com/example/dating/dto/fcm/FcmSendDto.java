package com.example.dating.dto.fcm;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "/fcm RequestDto")
@AllArgsConstructor
public class FcmSendDto {

    @NotEmpty(message = "상대방의 이름을 입력해주세요")
    private String targetName;

    @NotEmpty(message = "알림 제목을 입력해주세요")
    private String title;

    @NotEmpty(message = "알림 내용을 입력해주세요")
    private String body;

    private Long chatRoomNo;

}