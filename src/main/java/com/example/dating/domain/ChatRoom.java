package com.example.dating.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHATROOM_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OTHER_MEMBER_ID")
    private Member otherMember;

    private String uuid;

    private String type;

    public ChatRoom(Member member, Member otherMember, String uuid, String type) {
        this.member = member;
        this.otherMember = otherMember;
        this.uuid = uuid;
        this.type = type;
    }
}
