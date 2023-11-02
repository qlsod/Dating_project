package com.example.dating.service;

import com.example.dating.domain.*;
import com.example.dating.dto.feed.FeedCardDto;
import com.example.dating.dto.feed.FeedDto;
import com.example.dating.dto.feedaction.FeedCommentDto;
import com.example.dating.dto.feedaction.FeedActionDto;
import com.example.dating.exception.EntityNotFoundException;
import com.example.dating.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final MemberRepository memberRepository;
    private final FeedLikeRepository feedLikeRepository;
    private final FeedBookmarkRepository feedBookmarkRepository;
    private final FeedCommentRepository feedCommentRepository;

    @Transactional
    public void post(String email, FeedDto feedDto) throws Exception {
        Member myMember = memberRepository.findMyMember(email);

        if (myMember == null) {
            throw new Exception();
        }

        Feed feed = new Feed();
        feed.DtoToEntity(myMember, feedDto);

        feedRepository.save(feed);
    }

    public List<FeedCardDto> getList() {
        return feedRepository.findAllOrderByIdDesc();
    }

    @Transactional
    public void performAction(FeedActionDto feedActionDto, String email, String actionName) throws EntityNotFoundException {
        Optional<Feed> feedOptional = feedRepository.findById(feedActionDto.getFeedId());

        feedOptional.ifPresentOrElse(feed -> {
            Member myMember = memberRepository.findMyMember(email);
            cntPlusAndSaveEntity(actionName, feed, myMember, feedActionDto.getContent());
        }, () -> {
            throw new EntityNotFoundException("존재하지 않는 피드입니다.");
        });
    }

    public List<FeedCommentDto> getCommentList(Long feedId) throws EntityNotFoundException {
        // 존재하지 않는 피드 ID 라면
        Optional<Feed> feedOptional = feedRepository.findById(feedId);
        if (feedOptional.isEmpty()) {
            throw new EntityNotFoundException("존재하지 않는 피드입니다.");
        }

        return feedCommentRepository.findAllById(feedId);
    }

    private void cntPlusAndSaveEntity(String actionName, Feed feed, Member myMember, String content) {
        if (actionName.equals("북마크")) {
            feed.addBookmark();
            FeedBookmark feedBookmark = new FeedBookmark(feed, myMember);
            feedBookmarkRepository.save(feedBookmark);
        } else if (actionName.equals("좋아요")) {
            feed.addLike();
            FeedLike feedLike = new FeedLike(feed, myMember);
            feedLikeRepository.save(feedLike);
        } else {
            feed.addComment();
            FeedComment feedComment = new FeedComment(feed, myMember, content);
            feedCommentRepository.save(feedComment);
        }
    }
}
