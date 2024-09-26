package com.example.dating.controller;

import com.example.dating.dto.setting.ChangePasswordDto;
import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.service.SettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @Operation(summary = "비밀번호 변경",
            description = "현재 PW, 바꾸고 싶은 PW 입력하여 비밀번호 변경")
    @SecurityRequirement(name = "accessToken")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @PostMapping("/password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                              @RequestBody @Valid ChangePasswordDto changePasswordDto) {

        String email = principalDetails.getUsername();
        settingService.changePassword(email, changePasswordDto);
        return ResponseEntity.ok().build();

    }
}
