package com.example.dating.dto.block;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BlockListDto {
    private Long memberId;
    private String image;
    private String name;
    private String residence;
    private Integer age;
    private Integer height;
}
