package com.example.dating.controller;

import com.example.dating.dto.fcm.FcmSendDto;
import com.example.dating.service.FcmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/fcm")
@RequiredArgsConstructor
@Slf4j
public class FcmNotificationController {

    private final FcmService fcmService;


    @Operation(summary = "FCM 알림 보내기",
            description = "채팅, 좋아요 표시 알림")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @PostMapping("")
    public ResponseEntity<Void> sendNotificationByToken(@RequestBody @Valid FcmSendDto fcmSendDto) {

        log.info("fcmRequest");
        fcmService.sendPush(fcmSendDto);
        return ResponseEntity.ok().build();
    }

}
