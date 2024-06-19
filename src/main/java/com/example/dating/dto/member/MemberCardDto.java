package com.example.dating.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberCardDto {
    private Long id;
    private String nickName;
    private String address;
    private Integer age;
    private Integer height;
    private String image;
}
