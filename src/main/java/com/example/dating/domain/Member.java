package com.example.dating.domain;

import com.example.dating.dto.MemberInfoDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    private String name;
    private Integer age;
    private String gender;
    private String residence;
    private String image;
    private Integer height;
    private String mbti;
    private String personality;
    private String hobby;
    private String likeMbti;
    private String likePersonality;

    public Member() {
    }

    public void createMember(MemberInfoDto memberInfoDto, Account account) {
        this.account = account;
        this.name = memberInfoDto.getName();
        this.age = memberInfoDto.getAge();
        this.gender = memberInfoDto.getGender();
        this.residence = memberInfoDto.getResidence();
        this.image = memberInfoDto.getImage();
        this.height = memberInfoDto.getHeight();
        this.mbti = memberInfoDto.getMbti();
        this.personality = memberInfoDto.getPersonality();
        this.hobby = memberInfoDto.getHobby();
        this.likeMbti = memberInfoDto.getLikeMbti();
        this.likePersonality = memberInfoDto.getLikePersonality();
    }
}
