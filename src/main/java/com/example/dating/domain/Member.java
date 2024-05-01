package com.example.dating.domain;

import com.example.dating.dto.member.MemberInfoDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDay;
    private String email;
    private String password;
    private String nickName;
    private String description;
    private String gender;
    private String address;
    private Integer age;
    private Integer height;
    private String image;
    private String personalInfo;
//    private String mbti;
    private String personality;
    private String interest;
    private String likePersonality;

    public void mapDtoToEntity(MemberInfoDto memberInfoDto) {
        this.nickName = memberInfoDto.getNickName();
        this.description = memberInfoDto.getDescription();
        this.address = memberInfoDto.getAddress();
        this.age = memberInfoDto.getAge();
        this.height = memberInfoDto.getHeight();
        this.image = memberInfoDto.getImage();
        this.birthDay = memberInfoDto.getBirthDay();
        this.personalInfo = memberInfoDto.getPersonalInfo();
//        this.mbti = memberInfoDto.getMbti();
        this.gender = memberInfoDto.getGender();
        this.personality = memberInfoDto.getPersonality();
        this.interest = memberInfoDto.getInterest();
        this.likePersonality = memberInfoDto.getLikePersonality();
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
