package com.example.dating.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class MemberDto {

    @NotEmpty(message = "이름을 입력해주세요")
    private String name;

    @NotNull(message = "나이를 입력해주세요")
    private int age;

    @NotEmpty(message = "성별을 입력해주세요")
    private String gender;

    @NotEmpty(message = "사는 곳을 입력해주세요")
    private String residence;

    @NotEmpty(message = "사진을 입력해주세요")
    private String image;

    @NotNull(message = "키를 입력해주세요")
    private int height;

    @NotEmpty(message = "mbti를 입력해주세요")
    private String mbti;

    @NotEmpty(message = "성격을 입력해주세요")
    private String personality;

    @NotEmpty(message = "취미를 입력해주세요")
    private String hobby;

    @NotEmpty(message = "원하는 mbti를 입력해주세요")
    private String likeMbti;

    @NotEmpty(message = "원하는 성격을 입력해주세요")
    private String likePersonality;
}

