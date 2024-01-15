package com.example.dating.dto.member;

import com.example.dating.domain.Member;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class MemberInfoDto {

    @NotEmpty(message = "이름을 입력해주세요")
    private String name;

    @NotEmpty(message = "한줄 소개를 입력해주세요")
    private String comment;

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

    public void mapEntityToDto(Member member) {
        this.name = member.getName();
        this.comment = member.getComment();
        this.residence = member.getResidence();
        this.age = member.getAge();
        this.height = member.getHeight();
        this.image = member.getImage();
        this.personalInfo = member.getPersonalInfo();
        this.mbti = member.getMbti();
        this.personality = member.getPersonality();
        this.interest = member.getInterest();
        this.likePersonality = member.getLikePersonality();
    }
}

