package com.example.dating.domain;

import com.example.dating.dto.member.MemberInfoDto;
import com.example.dating.dto.search.SearchDto;
import com.example.dating.dto.search.SearchRes;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Search {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    public void mapDtoToEntity(SearchDto searchDto) {
        this.title = searchDto.getTitle();
        this.content = searchDto.getContent();
    }


    public Search(Member member) {
        this.member = member;
    }

}
