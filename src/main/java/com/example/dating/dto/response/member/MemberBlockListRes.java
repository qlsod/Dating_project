package com.example.dating.dto.response.member;

import com.example.dating.dto.block.BlockListDto;
import lombok.Data;

import java.util.List;

@Data
public class MemberBlockListRes {
    private List<BlockListDto> blockMemberList;
}
