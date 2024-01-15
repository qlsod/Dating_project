package com.example.dating.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class RoomMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROOMMEMBER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GWATINGROOM_ID")
    private GwatingRoom gwatingRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public RoomMember(GwatingRoom gwatingRoom, Member member) {
        this.gwatingRoom = gwatingRoom;
        this.member = member;
    }
}
