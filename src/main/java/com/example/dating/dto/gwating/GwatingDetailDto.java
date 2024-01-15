package com.example.dating.dto.gwating;

import com.example.dating.dto.member.MemberInviteDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GwatingDetailDto {
    private Long id;
    private String image;
    private String roomName;
    private String roomDescription;
    private Integer maleCount;
    private Integer joinMaleCount;
    private Integer femaleCount;
    private Integer joinFemaleCount;
    private String roomCategory;
    private String location;
    private List<MemberInviteDto> memberInviteDtoList;

    public GwatingDetailDto(Long id, String image, String roomName, String roomDescription, Integer maleCount, Integer joinMaleCount, Integer femaleCount, Integer joinFemaleCount, String roomCategory, String location) {
        this.id = id;
        this.image = image;
        this.roomName = roomName;
        this.roomDescription = roomDescription;
        this.maleCount = maleCount;
        this.joinMaleCount = joinMaleCount;
        this.femaleCount = femaleCount;
        this.joinFemaleCount = joinFemaleCount;
        this.roomCategory = roomCategory;
        this.location = location;
    }

    public void setMemberInviteDtoList(List<MemberInviteDto> memberInviteDtoList) {
        this.memberInviteDtoList = memberInviteDtoList;
    }
}
