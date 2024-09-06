package com.example.dating.dto.search;

import com.example.dating.domain.Search;
import com.example.dating.dto.member.MemberCommonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchRes {

    private String title;
    private Long id;
    private MemberCommonDto user;

}
