package com.example.dating.dto.gwating;

import com.example.dating.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GwatingMemberDto {

    private List<Member> memberList;
}
