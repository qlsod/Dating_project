package com.example.dating.dto.gwating;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class GwatingCardDto {
    private Long id;
    private String roomName;
    private Integer maleCount;
    private Integer joinMaleCount;
    private Integer femaleCount;
    private Integer joinFemaleCount;
    private String roomCategory;
    private String location;

    public GwatingCardDto(Long id, String roomName, Integer maleCount, Integer joinMaleCount, Integer femaleCount, Integer joinFemaleCount, String roomCategory, String location) {
        this.id = id;
        this.roomName = roomName;
        this.maleCount = maleCount;
        this.joinMaleCount = joinMaleCount;
        this.femaleCount = femaleCount;
        this.joinFemaleCount = joinFemaleCount;
        this.roomCategory = roomCategory;
        this.location = location;
    }
}
