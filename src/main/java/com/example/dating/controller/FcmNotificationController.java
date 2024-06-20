package com.example.dating.controller;

import com.example.dating.dto.Fcm.FcmSendDto;
import com.example.dating.service.FcmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fcm")
@RequiredArgsConstructor
public class FcmNotificationController {

    private final FcmService fcmService;


    @Operation(summary = "FCM 알림 보내기",
            description = "채팅, 좋아요 표시 알림")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @PostMapping("")
    public ResponseEntity<Void> sendNotificationByToken(@RequestBody FcmSendDto fcmSendDto) {

        fcmService.sendPush(fcmSendDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
