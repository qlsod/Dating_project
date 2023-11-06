package com.example.dating.repository;

import com.example.dating.domain.FeedComment;
import com.example.dating.dto.feedaction.FeedCommentDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {
    @Query("select new com.example.dating.dto.feedaction.FeedCommentDto(fc.member.id, fc.member.name, fc.member.image, fc.content, fc.createAt) " +
            "from FeedComment fc where fc.feed.id = :feedId")
    List<FeedCommentDto> findAllById(@Param("feedId") Long feedId);
}
