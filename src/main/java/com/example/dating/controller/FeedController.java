package com.example.dating.controller;

import com.example.dating.dto.feed.FeedCardDto;
import com.example.dating.dto.feed.FeedDto;
import com.example.dating.dto.feedaction.FeedActionDto;
import com.example.dating.dto.feedaction.FeedCommentDto;
import com.example.dating.exception.EntityNotFoundException;
import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed")
public class FeedController {

    private final FeedService feedService;

    @PostMapping("/post")
    public ResponseEntity<String> postFeed(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                           @Validated @RequestBody FeedDto feedDto,
                                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }

        try {
            feedService.post(principalDetails.getUsername(), feedDto);
            return ResponseEntity.ok("피드 올리기에 성공했습니다.");
        } catch (DataAccessException e) {
            return ResponseEntity.internalServerError().body("데이터베이스 오류로 피드를 올릴 수 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("피드 올리기에 실패했습니다.");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getList() {
        try {
            List<FeedCardDto> list = feedService.getList();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("피드 불러오기에 실패했습니다.");
        }
    }

    @GetMapping("/comment/list")
    public ResponseEntity<Object> getCommentList(@RequestParam Long feedId) {
        try {
            List<FeedCommentDto> commentList = feedService.getCommentList(feedId);
            return ResponseEntity.ok(commentList);
        }  catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/comment")
    public ResponseEntity<String> postComment(@RequestBody FeedActionDto feedLikeDto,
                                              @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return clickAction(feedLikeDto, principalDetails, "댓글");
    }

    @PostMapping("/like")
    public ResponseEntity<String> clickLike(@RequestBody FeedActionDto feedLikeDto,
                                            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return clickAction(feedLikeDto, principalDetails, "좋아요");
    }

    @PostMapping("/bookmark")
    public ResponseEntity<String> clickBookmark(@RequestBody FeedActionDto feedBookmarkDto,
                                                @AuthenticationPrincipal PrincipalDetails principalDetails) {
       return clickAction(feedBookmarkDto, principalDetails, "북마크");
    }

    private ResponseEntity<String> clickAction(FeedActionDto feedActionDto,
                                               PrincipalDetails principalDetails,
                                               String actionName) {
        try {
            feedService.performAction(feedActionDto, principalDetails.getUsername(), actionName);
            return ResponseEntity.ok(actionName + " 성공");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
