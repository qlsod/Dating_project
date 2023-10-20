package com.example.dating.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberMbtiDto {

    private Long id;
    private String name;
    private String mbti;

    public MemberMbtiDto(Long id, String mbti) {
        this.id = id;
        this.mbti = mbti;
    }
}
