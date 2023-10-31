package com.example.dating.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class FeedLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FEEDLIKE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FEED_ID")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public FeedLike(Feed feed, Member member) {
        this.feed = feed;
        this.member = member;
    }
}
