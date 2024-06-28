package com.example.dating.dto.setting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {

    @NotEmpty(message = "현재 비밀번호를 입력해주세요.")
    private String currentPassword;

    @NotEmpty(message = "새 비밀번호를 입력해주세요.")
    @Pattern(
            regexp = "^{8,20}$",
            message = "비밀번호는 8자이상 20자 이하입니다."
    )
    private String newPassword;

    @NotEmpty(message = "비밀번호를 한 번 더 입력해주세요.")
    private String checkNewPassword;
}
