package com.example.dating.controller;

import com.example.dating.exception.DuplicateDataException;
import com.example.dating.exception.EntityNotFoundException;
import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.service.HeartService;
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

    @PostMapping("/heart/add")
    public ResponseEntity<Map<String, String>> heart(@RequestParam Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        HashMap<String, String> response = new HashMap<>();

        try {
            heartService.heart(id, principalDetails.getUsername());
            response.put("successMessage", "하트 보내기 성공");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
