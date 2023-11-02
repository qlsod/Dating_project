package com.example.dating.repository;

import com.example.dating.domain.Feed;
import com.example.dating.dto.feed.FeedCardDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    @Query("select new com.example.dating.dto.feed.FeedCardDto(f.id, f.member.name, f.member.residence, f.member.age, f.member.height, f.member.image, " +
            "f.image, f.content, f.hashTag, f.feedComment, f.feedLike, f.feedBookmark, f.updatedAt) from Feed f order by f.id desc")
    List<FeedCardDto> findAllOrderByIdDesc();
}
