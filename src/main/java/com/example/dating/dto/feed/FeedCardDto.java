package com.example.dating.dto.feed;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FeedCardDto {
    private Long id;
    private String name;
    private String residence;
    private Integer age;
    private Integer height;
    private String profileImage;
    private String feedImage;
    private String content;
    private String hashTag;
    private Integer commentCnt;
    private Integer likeCnt;
    private Integer bookmarkCnt;
    private LocalDateTime updateAt;
}
