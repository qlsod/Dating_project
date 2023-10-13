package com.example.dating.repository;

import com.example.dating.domain.GwatingRoom;
import com.example.dating.dto.gwating.GwatingCardDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GwatingRepository extends JpaRepository<GwatingRoom, Long> {
    @Query("select new com.example.dating.dto.GwatingCardDto(g.id, g.roomName, g.ageCategory, g.maleCount, g.femaleCount) from GwatingRoom g where g.roomCategory = :roomCategory")
    List<GwatingCardDto> findAllByRoomCategory(@Param("roomCategory") String roomCategory);

    @Query("select g from GwatingRoom g where g.id = :id")
    GwatingRoom findOneById(@Param("id") Long id);
}
