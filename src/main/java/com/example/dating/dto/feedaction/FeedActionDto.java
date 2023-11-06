package com.example.dating.dto.feedaction;


import com.example.dating.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedActionDto {
    private Long feedId;
    private String content;

    public FeedActionDto(Long feedId) {
        this.feedId = feedId;
    }
}
