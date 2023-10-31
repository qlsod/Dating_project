package com.example.dating.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Feed {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MYFEED_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    private String image;
    private String content;
    private String hashTag;
    private Integer feedComment;
    private Integer feedLike;
    private Integer feedBookmark;

    public Feed(Member member, String image, String content, String hashTag, Integer feedComment, Integer feedLike, Integer feedBookmark) {
        this.member = member;
        this.image = image;
        this.content = content;
        this.hashTag = hashTag;
        this.feedComment = feedComment;
        this.feedLike = feedLike;
        this.feedBookmark = feedBookmark;
    }

    public void addComment() {
        feedComment += 1;
    }

    public void addLike() {
        feedLike += 1;
    }

    public void addBookmark() {
        feedBookmark += 1;
    }
}
