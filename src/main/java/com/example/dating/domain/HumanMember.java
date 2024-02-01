package com.example.dating.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class HumanMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HUMAN_MEMBER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public HumanMember(Member member) {
        this.member = member;
    }
}
