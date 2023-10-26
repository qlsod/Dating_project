package com.example.dating.controller;

import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    @PostMapping("/heart/add")
    public String heart(@RequestParam Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return heartService.heart(id, principalDetails.getUsername());
    }
}
