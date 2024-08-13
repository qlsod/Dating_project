package com.example.dating.dto.member;

import com.example.dating.domain.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCommonDto {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "MEMBER_ID")
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birthDay;
    private String nickName;
    private String description;
    private String gender;
    private String address;
    private Integer age;
    private Integer height;
    private String image;
    private List<String> images;
    private String personalInfo;
    private String personality;
    private String interest;
    private String likePersonality;


    public MemberCommonDto(Member member, List<String> images) {
        this.id = member.getId();
        this.nickName = member.getNickName();
        this.birthDay = member.getBirthDay();
        this.description = member.getDescription();
        this.address = member.getAddress();
        this.age = member.getAge();
        this.height = member.getHeight();
        this.image = member.getImage();
        this.personality = member.getPersonality();
        this.personalInfo = member.getPersonalInfo();
        this.interest = member.getInterest();
        this.likePersonality = member.getLikePersonality();
        this.gender = member.getGender();
        this.images = images;
    }
}