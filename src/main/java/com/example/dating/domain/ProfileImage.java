package com.example.dating.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ProfileImage {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private Member member;

    @Column(nullable = false)
    private String image;

    public ProfileImage(Member member, String image) {
        this.member = member;
        this.image = image;
    }

}