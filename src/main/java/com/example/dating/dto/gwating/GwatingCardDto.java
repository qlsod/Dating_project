package com.example.dating.dto.gwating;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class GwatingCardDto {
    private Long id;
    private String roomName;
    private String genderCategory = "성별 무관";
    private String ageCategory;
    private Integer maleCount;
    private Integer femaleCount;

    public GwatingCardDto(Long id, String roomName, String ageCategory, Integer maleCount, Integer femaleCount) {
        this.id = id;
        this.roomName = roomName;
        this.ageCategory = ageCategory;
        this.maleCount = maleCount;
        this.femaleCount = femaleCount;
    }
}
