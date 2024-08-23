package com.example.dating.dto.response;
import com.example.dating.dto.member.MemberCommonDto;
import lombok.Data;

import java.util.List;

@Data
public class HomeRes {
    private List<MemberCommonDto> randomMemberList;
    private List<MemberCommonDto> favoriteMemberList;
    private List<MemberCommonDto> fanMemberList;
}
