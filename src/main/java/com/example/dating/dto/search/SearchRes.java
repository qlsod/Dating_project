package com.example.dating.dto.search;

import com.example.dating.domain.Search;
import com.example.dating.dto.member.MemberCommonDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchRes {

    private String title;
    private String content;
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "created_at")
    private LocalDateTime createdAt; // 보낸 시간
    private MemberCommonDto user;

}
