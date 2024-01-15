package com.example.dating.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberInviteDto {
    private Long id;
    private String name;
    private Integer age;
    private String residence;
}
