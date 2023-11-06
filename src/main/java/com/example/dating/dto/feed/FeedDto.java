package com.example.dating.dto.feed;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
public class FeedDto {

    @NotEmpty(message = "이미지는 필수입니다.")
    private String feedImage;

    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;

    private String hashTag;
    private Integer commentCnt;
    private Integer likeCnt;
    private Integer bookmarkCnt;
}
