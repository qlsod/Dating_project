package com.example.dating.controller;

import com.example.dating.domain.Alert;
import com.example.dating.dto.alert.AlertDto;
import com.example.dating.dto.response.alert.AlertRes;
import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

//    @GetMapping("/alert")
//    public ResponseEntity<AlertRes> getAlert(@AuthenticationPrincipal PrincipalDetails principalDetails) {
//
//        try {
//
//            String email = principalDetails.getUsername();
//
//            AlertRes alertRes = new AlertRes();
//            alertRes.setImageList(alertService.getAlertList(email));
//
//            return ResponseEntity.ok(alertRes);
//        } catch (RuntimeException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
