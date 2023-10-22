package com.example.dating.dto.member;

import com.example.dating.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class MemberInfoDto {

    @NotEmpty(message = "이름을 입력해주세요")
    private String name;

    @NotEmpty(message = "한줄 소개를 입력해주세요")
    private String comment;

    @NotEmpty(message = "성별을 입력해주세요")
    private String gender;

    @NotEmpty(message = "사는 곳을 입력해주세요")
    private String residence;

    @NotNull(message = "나이를 입력해주세요")
    private int age;

    @NotNull(message = "키를 입력해주세요")
    private int height;

    @NotEmpty(message = "사진을 입력해주세요")
    private String image;

    @NotEmpty(message = "인적사항을 입력해주세요")
    private String personalInfo;

    @NotEmpty(message = "mbti를 입력해주세요")
    private String mbti;

    @NotEmpty(message = "성격을 입력해주세요")
    private String personality;

    @NotEmpty(message = "관심사를 입력해주세요")
    private String interest;

    @NotEmpty(message = "이상형을 입력해주세요")
    private String likePersonality;

    @Builder
    public MemberInfoDto(String name, String comment, String gender, String residence, int age, int height, String image, String personalInfo, String mbti, String personality, String interest, String likePersonality) {
        this.name = name;
        this.comment = comment;
        this.gender = gender;
        this.residence = residence;
        this.age = age;
        this.height = height;
        this.image = image;
        this.personalInfo = personalInfo;
        this.mbti = mbti;
        this.personality = personality;
        this.interest = interest;
        this.likePersonality = likePersonality;
    }
}

