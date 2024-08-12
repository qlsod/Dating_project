package com.example.dating.dto.response.member;

import lombok.Data;

@Data
public class MemberJoinRes {

    private Long memberId;

    public MemberJoinRes(Long memberId) {
        this.memberId = memberId;
    }
}

