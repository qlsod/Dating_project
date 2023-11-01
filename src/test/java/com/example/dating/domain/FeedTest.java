package com.example.dating.domain;

import com.example.dating.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FeedTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private FeedCommentRepository feedCommentRepository;

    @Autowired
    private FeedLikeRepository feedLikeRepository;

    @Autowired
    private FeedBookmarkRepository feedBookmarkRepository;

    @BeforeEach
    void before() {
        Account account1 = new Account("test1@test.com", "test1234");
        Account account2 = new Account("test2@test.com", "test1234");
        accountRepository.save(account1);
        accountRepository.save(account2);

        Member member1 = new Member(account1, "김포로", "진지한 만남 원해요!",
                "남자", "서울 강서구", 24, 183, "이미지 경로",
                "전문대,학생,쿨한", "ENTP", "털털한,착한,열정적인",
                "LOL,코딩,K-POP,전시회", "털털한,착한,겁많은");

        Member member2 = new Member(account2, "김로포", "진지한 만남 원해요!",
                "여자", "서울 동대문구", 28, 162, "이미지 경로",
                "전문대,학생,쿨한", "ENTP", "털털한,착한,열정적인",
                "LOL,코딩,K-POP,전시회", "털털한,착한,겁많은");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Feed myFeed = new Feed(member1, "이미지경로", "피드내용", "해시태그",0, 0, 0);
        feedRepository.save(myFeed);
    }

    @Test
    void 피드전체조회() {
        int size = feedRepository.findFeeds().size();
        Assertions.assertThat(size).isEqualTo(1);
    }

    @Test
    void 피드_댓글() {
        // given
        Member member2 = memberRepository.findById(2L).get();
        Feed feed = feedRepository.findById(1L).get();

        // when
        FeedComment feedComment = new FeedComment(feed, member2, "member1 피드의 댓글입니다.");
        feedCommentRepository.save(feedComment);
        feed.addComment();

        // then
        FeedComment findFeedComment = feedCommentRepository.findById(1L).get();

        Assertions.assertThat(findFeedComment.getContent()).isEqualTo(feedComment.getContent());
        Assertions.assertThat(feed.getFeedComment()).isEqualTo(1);
    }

    @Test
    void 피드_좋아요() {
        // given
        Member member2 = memberRepository.findById(2L).get();
        Feed feed = feedRepository.findById(1L).get();

        // when
        FeedLike feedLike = new FeedLike(feed, member2);
        feedLikeRepository.save(feedLike);
        feed.addLike();

        // then
        FeedLike findFeedLike = feedLikeRepository.findById(1L).get();

        Assertions.assertThat(feed.getFeedLike()).isEqualTo(1);
        Assertions.assertThat(findFeedLike.getMember().getId()).isEqualTo(2L);
    }

    @Test
    void 피드_북마크() {
        // given
        Member member2 = memberRepository.findById(2L).get();
        Feed feed = feedRepository.findById(1L).get();

        // when
        FeedBookmark feedBookmark = new FeedBookmark(feed, member2);
        feedBookmarkRepository.save(feedBookmark);
        feed.addBookmark();

        // then
        FeedBookmark findFeedBookmark = feedBookmarkRepository.findById(1L).get();

        Assertions.assertThat(feed.getFeedBookmark()).isEqualTo(1);
        Assertions.assertThat(findFeedBookmark.getMember().getId()).isEqualTo(2L);
    }
}