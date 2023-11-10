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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed")
public class FeedController {

    private final FeedService feedService;

    @PostMapping("/post")
    public ResponseEntity<Map<String, String>> postFeed(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                        @Validated @RequestBody FeedDto feedDto,
                                                        BindingResult bindingResult) {

        HashMap<String, String> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            response.put("errorMessage", bindingResult.getFieldError().getDefaultMessage());
            return ResponseEntity.badRequest().body(response);
        }

        try {
            feedService.post(principalDetails.getUsername(), feedDto);
            response.put("successMessage", "피드 올리기에 성공했습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("errorMessage", "피드 올리기에 실패했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getList() {
        try {
            List<FeedCardDto> list = feedService.getList();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            HashMap<String, String> response = new HashMap<>();
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/comment/list")
    public ResponseEntity<Object> getCommentList(@RequestParam Long feedId) {
        try {
            List<FeedCommentDto> commentList = feedService.getCommentList(feedId);
            return ResponseEntity.ok(commentList);
        }  catch (EntityNotFoundException e) {
            HashMap<String, String> response = new HashMap<>();
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/comment")
    public ResponseEntity<Map<String, String>> postComment(@RequestBody FeedActionDto feedLikeDto,
                                              @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return clickAction(feedLikeDto, principalDetails, "댓글");
    }

    @PostMapping("/like")
    public ResponseEntity<Map<String, String>> clickLike(@RequestBody FeedActionDto feedLikeDto,
                                            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return clickAction(feedLikeDto, principalDetails, "좋아요");
    }

    @PostMapping("/bookmark")
    public ResponseEntity<Map<String, String>> clickBookmark(@RequestBody FeedActionDto feedBookmarkDto,
                                                @AuthenticationPrincipal PrincipalDetails principalDetails) {
       return clickAction(feedBookmarkDto, principalDetails, "북마크");
    }

    private ResponseEntity<Map<String, String>> clickAction(FeedActionDto feedActionDto,
                                               PrincipalDetails principalDetails,
                                               String actionName) {
        HashMap<String, String> response = new HashMap<>();
        try {
            feedService.performAction(feedActionDto, principalDetails.getUsername(), actionName);
            response.put("successMessage", actionName + " 성공");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
