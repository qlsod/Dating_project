package com.example.dating.repository;

import com.example.dating.domain.ProfileImage;
import com.example.dating.domain.Search;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchRepository extends JpaRepository<Search, Long> {


    @Query("SELECT s FROM Search s WHERE id = :id")
    Search findSearchById(@Param("id") Long id);


}

