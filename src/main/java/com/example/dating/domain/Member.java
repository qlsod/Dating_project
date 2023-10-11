package com.example.dating.domain;

import com.example.dating.dto.MemberDto;
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

    public void createMember(MemberDto memberDto) {
        this.name = memberDto.getName();
        this.age = memberDto.getAge();
        this.gender = memberDto.getGender();
        this.residence = memberDto.getResidence();
        this.image = memberDto.getImage();
        this.height = memberDto.getHeight();
        this.mbti = memberDto.getMbti();
        this.personality = memberDto.getPersonality();
        this.hobby = memberDto.getHobby();
        this.likeMbti = memberDto.getLikeMbti();
        this.likePersonality = memberDto.getLikePersonality();
    }
}
