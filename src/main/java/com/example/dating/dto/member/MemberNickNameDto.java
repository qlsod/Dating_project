package com.example.dating.dto.member;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class MemberNickNameDto {

    @NotEmpty(message = "이름을 입력해주세요")
    private String nickName;

}
