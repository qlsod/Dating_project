package com.example.dating.dto.response.member;

import lombok.Data;

@Data
public class MemberMailRes {
    private String code;

    public MemberMailRes(String code){
        this.code = code;
    }
}
