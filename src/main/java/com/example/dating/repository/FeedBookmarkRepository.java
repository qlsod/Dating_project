package com.example.dating.repository;

import com.example.dating.domain.FeedBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedBookmarkRepository extends JpaRepository<FeedBookmark, Long> {
}
