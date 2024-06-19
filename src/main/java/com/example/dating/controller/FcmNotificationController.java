package com.example.dating.controller;

import com.example.dating.dto.Fcm.FcmSendDto;
import com.example.dating.service.FcmService;
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

    @PostMapping("")
    public ResponseEntity<Void> sendNotificationByToken(@RequestBody FcmSendDto fcmSendDto) {

        fcmService.sendPush(fcmSendDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
