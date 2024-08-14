package com.example.dating.controller;

import com.example.dating.exception.DuplicateDataException;
import com.example.dating.exception.EntityNotFoundException;
import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.service.HeartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    @Operation(summary = "하트 보내기",
            description = "다른 사용자의 uuid 받아 하트 저장")
    @SecurityRequirement(name = "accessToken")
    @PostMapping("/heart/add")
    public ResponseEntity<Void> heart(@RequestParam Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        try {
            heartService.heart(id, principalDetails.getUsername());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
