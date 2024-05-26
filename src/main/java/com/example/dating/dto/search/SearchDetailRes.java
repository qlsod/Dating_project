package com.example.dating.dto.search;


import com.example.dating.domain.Search;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchDetailRes {

    private String nickName;
    private String address;
    private String title;
    private String content;
    private Integer age;
    private Integer height;
    private Long memberId;

    public void entityToDto(Search search) {
        this.nickName = search.getMember().getNickName();
        this.address = search.getMember().getAddress();
        this.title = search.getTitle();
        this.age = search.getMember().getAge();
        this.content = search.getContent();
        this.height = search.getMember().getHeight();
        this.memberId = search.getMember().getId();
    }


}
