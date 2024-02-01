package com.example.dating.repository;

import com.example.dating.domain.Feed;
import com.example.dating.dto.feed.FeedCardDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    @Query("select new com.example.dating.dto.feed.FeedCardDto(f.id, f.member.name, f.member.residence, f.member.age, f.member.height, f.member.image, " +
            "f.image, f.content, f.hashTag, f.feedComment, f.feedLike, f.feedBookmark, f.updatedAt) from Feed f " +
            "WHERE f.member.id NOT IN (SELECT b.blockMember.id FROM Block b WHERE b.blockItMember.email = :email) OR f.member.id NOT IN (SELECT b.blockItMember.id FROM Block b WHERE b.blockMember.email = :email) order by f.id desc")
    List<FeedCardDto> findAllOrderByIdDesc(@Param("email") String email);
}
