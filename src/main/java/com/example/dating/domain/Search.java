package com.example.dating.domain;

import com.example.dating.dto.member.MemberInfoDto;
import com.example.dating.dto.search.SearchDto;
import com.example.dating.dto.search.SearchPatchDto;
import com.example.dating.dto.search.SearchRes;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Search {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt; // 보낸 시간

    public void mapDtoToEntity(SearchDto searchDto) {
        this.title = searchDto.getTitle();
        this.content = searchDto.getContent();
        this.createdAt = searchDto.getCreatedAt();
    }

    public void updateEntity(SearchPatchDto searchPatchDto) {
        this.title = searchPatchDto.getTitle();
        this.content = searchPatchDto.getContent();
        this.createdAt = searchPatchDto.getCreatedAt();
    }


    public Search(Member member) {
        this.member = member;
    }

}
