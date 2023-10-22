package com.example.dating.domain;

import com.example.dating.dto.member.MemberInfoDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    private String name;
    private String comment;
    private String gender;
    private String residence;
    private Integer age;
    private Integer height;
    private String image;
    private String personalInfo;
    private String mbti;
    private String personality;
    private String interest;
    private String likePersonality;

    public void mapDtoToEntity(MemberInfoDto memberInfoDto) {
        this.name = memberInfoDto.getName();
        this.comment = memberInfoDto.getComment();
        this.gender = memberInfoDto.getGender();
        this.residence = memberInfoDto.getResidence();
        this.age = memberInfoDto.getAge();
        this.height = memberInfoDto.getHeight();
        this.image = memberInfoDto.getImage();
        this.personalInfo = memberInfoDto.getPersonalInfo();
        this.mbti = memberInfoDto.getMbti();
        this.personality = memberInfoDto.getPersonality();
        this.interest = memberInfoDto.getInterest();
        this.likePersonality = memberInfoDto.getLikePersonality();
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
