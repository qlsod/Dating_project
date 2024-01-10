package com.example.dating.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberMbtiDto {

    private Long id;
    private String name;
    private String mbti;
    private String comment;

    public MemberMbtiDto(Long id, String mbti, String comment) {
        this.id = id;
        this.mbti = mbti;
        this.comment = comment;
    }
}
