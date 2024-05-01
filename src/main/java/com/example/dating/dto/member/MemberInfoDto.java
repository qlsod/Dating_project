package com.example.dating.dto.member;

import com.example.dating.domain.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MemberInfoDto {

    @NotEmpty(message = "이름을 입력해주세요")
    private String nickName;

//    @NotEmpty(message = "한줄 소개를 입력해주세요")
    private String description;

    @NotNull(message = "생일을 입력해주세요")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDay;

    @NotEmpty(message = "사는 곳을 입력해주세요")
    private String address;

    @NotEmpty(message = "성별을 입력해주세요")
    private String gender;

//    @NotNull(message = "나이를 입력해주세요")
    private int age;

//    @NotNull(message = "키를 입력해주세요")
    private int height;

//    @NotEmpty(message = "사진을 입력해주세요")
    private List<String> images;

//    @NotEmpty(message = "대표 이미지를 입력해주세요")
    private String image;

//    @NotEmpty(message = "인적사항을 입력해주세요")
    private String personalInfo;

//    @NotEmpty(message = "mbti를 입력해주세요")
//    private String mbti;

//    @NotEmpty(message = "성격을 입력해주세요")
    private String personality;

//    @NotEmpty(message = "관심사를 입력해주세요")
    private String interest;

//    @NotEmpty(message = "이상형을 입력해주세요")
    private String likePersonality;

    public void mapEntityToDto(Member member) {
        this.nickName = member.getNickName();
        this.description = member.getDescription();
        this.birthDay = member.getBirthDay();
        this.address = member.getAddress();
        this.age = member.getAge();
        this.height = member.getHeight();
        this.image = member.getImage();
        this.personalInfo = member.getPersonalInfo();
        this.gender = member.getGender();
//        this.mbti = member.getMbti();
        this.personality = member.getPersonality();
        this.interest = member.getInterest();
        this.likePersonality = member.getLikePersonality();
    }


}