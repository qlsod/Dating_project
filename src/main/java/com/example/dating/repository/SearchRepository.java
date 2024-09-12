package com.example.dating.repository;

import com.example.dating.domain.ProfileImage;
import com.example.dating.domain.Search;
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


}

