package com.example.dating.dto.search;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SearchHistoryDto {

    @NotEmpty(message = "해당 유저의 닉네임을 입력해주세요")
    private String nickName;
}
