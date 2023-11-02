package com.example.dating.dto.feedaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FeedCommentDto {
    private Long memberId;
    private String name;
    private String memberImage;
    private String content;
    private LocalDateTime createAt;
}
