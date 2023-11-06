package com.example.dating.domain;

import com.example.dating.dto.feed.FeedDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 테스트 용 생성자
    public Feed(Member member, String image, String content, String hashTag, Integer feedComment, Integer feedLike, Integer feedBookmark) {
        this.member = member;
        this.image = image;
        this.content = content;
        this.hashTag = hashTag;
        this.feedComment = feedComment;
        this.feedLike = feedLike;
        this.feedBookmark = feedBookmark;
    }

    public void DtoToEntity(Member member, FeedDto feedDto) {
        this.member = member;
        this.image = feedDto.getFeedImage();
        this.content = feedDto.getContent();
        this.hashTag = feedDto.getHashTag();
        this.feedComment = 0;
        this.feedLike = 0;
        this.feedBookmark = 0;
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
