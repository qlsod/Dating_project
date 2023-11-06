package com.example.dating.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class FeedComment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FEEDCOMMENT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FEED_ID")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createAt;

    public FeedComment(Feed feed, Member member, String content) {
        this.feed = feed;
        this.member = member;
        this.content = content;
    }
}
