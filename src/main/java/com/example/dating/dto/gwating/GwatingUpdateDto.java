package com.example.dating.dto.gwating;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GwatingUpdateDto {

    @NotEmpty(message = "방 제목을 입력해주세요.")
    private String roomName;

    @NotEmpty(message = "방 설명을 입력해주세요.")
    private String roomDescription;
}
