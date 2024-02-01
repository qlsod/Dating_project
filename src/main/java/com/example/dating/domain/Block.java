package com.example.dating.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BLOCK_ID")
    private Long id;

    // 차단한 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BLOCK_IT_MEMBER_ID")
    private Member blockItMember;

    // 차단 당한 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BLOCK_MEMBER_ID")
    private Member blockMember;

    public Block(Member blockItMember, Member blockMember) {
        this.blockItMember = blockItMember;
        this.blockMember = blockMember;
    }

}
