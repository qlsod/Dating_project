package com.example.dating.controller;

import com.example.dating.dto.setting.ChangePasswordDto;
import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/setting")
@RequiredArgsConstructor
public class SettingController {

    private final SettingService settingService;

    @GetMapping("/account")
    public ResponseEntity<Object> checkAccount(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        HashMap<String, String> response = new HashMap<>();

        try {
            response.put("email", principalDetails.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                              @Validated @RequestBody ChangePasswordDto changePasswordDto,
                                                              BindingResult bindingResult) {
        HashMap<String, String> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            response.put("errorMessage", bindingResult.getFieldError().getDefaultMessage());
            return ResponseEntity.badRequest().body(response);
        }

        try {
            String email = principalDetails.getUsername();
            settingService.changePassword(email, changePasswordDto);
            response.put("successMessage", "비밀번호 변경 성공");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
