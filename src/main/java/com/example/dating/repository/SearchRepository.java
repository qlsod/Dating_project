package com.example.dating.repository;

import com.example.dating.domain.Search;
import com.example.dating.dto.search.SearchHistoryRes;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchRepository extends JpaRepository<Search, Long> {


    @Query("select s from Search s where id = :id")
    Search findSearchById(@Param("id") Long id);

    @Query("select s from Search s where s.id < :id order by s.id desc")
    List<Search> findPagingSearch(@Param("id") Long id, PageRequest pageRequest);

    @Query("select s from Search s order by s.id desc")
    List<Search> findPagingSearchFirst(PageRequest pageRequest);

    @Query("select s from Search s where s.member.email = :email and s.id = :id")
    Search checkAuthor(@Param("email") String email, @Param("id") Long id);


    @Query("select new com.example.dating.dto.search.SearchHistoryRes(s.id, s.title, s.content, s.createdAt) from Search s where s.member.nickName = :nickName order by s.createdAt desc")
    List<SearchHistoryRes> findSearchHistory(@Param("nickName") String nickName);

}

