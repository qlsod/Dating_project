package com.example.dating.dto.gwating;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class GwatingRoomDto {

    @NotEmpty(message = "방 제목을 입력해주세요.")
    private String roomName;

    @NotNull(message = "인원수를 입력해주세요.")
    private Integer allCount;

    @NotEmpty(message = "방 카테고리를 지정해주세요. (대학생, 일반)")
    private String roomCategory;

    @NotEmpty(message = "나이대를 지정해주세요.")
    private String ageCategory;

    @NotEmpty(message = "만날 위치를 입력해주세요.")
    private String location;
}
