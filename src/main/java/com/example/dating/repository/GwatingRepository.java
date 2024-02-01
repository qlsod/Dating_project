package com.example.dating.repository;

import com.example.dating.domain.GwatingRoom;
import com.example.dating.dto.gwating.GwatingCardDto;
import com.example.dating.dto.gwating.GwatingDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface GwatingRepository extends JpaRepository<GwatingRoom, Long> {
    @Query("select new com.example.dating.dto.gwating.GwatingCardDto(g.id, g.roomName, g.maleCount, g.joinMaleCount, g.femaleCount, g.joinFemaleCount, g.roomCategory, g.location) from GwatingRoom g " +
            "JOIN RoomMember rm ON rm.gwatingRoom.id = g.id WHERE g.roomCategory = :roomCategory AND rm.member.id NOT IN (SELECT b.blockMember.id FROM Block b WHERE b.blockItMember.email = :email)")
    List<GwatingCardDto> findAllByRoomCategory(@Param("roomCategory") String roomCategory, @Param("email") String email);

    @Query("select new com.example.dating.dto.gwating.GwatingDetailDto(g.id, g.image, g.roomName, g.roomDescription , g.maleCount, g.joinMaleCount, g.femaleCount, g.joinFemaleCount, g.roomCategory, g.location) from GwatingRoom g " +
    "where g.id = :gwatingRoomId")
    GwatingDetailDto findOneByIdToGwatingDetailDto(@Param("gwatingRoomId") Long gwatingRoomId);

    @Query("select g from GwatingRoom g where g.id = :id")
    GwatingRoom findOneById(@Param("id") Long id);

    @Query("select r.member.id from RoomMember r where r.gwatingRoom = :gwatingRoom")
    List<Long> findJoinMemberList(@Param("gwatingRoom") GwatingRoom gwatingRoom);

    @Query("select new com.example.dating.dto.gwating.GwatingCardDto(g.id, g.roomName, g.maleCount, g.joinMaleCount, g.femaleCount, g.joinFemaleCount, g.roomCategory, g.location) from GwatingRoom g " +
            "JOIN RoomMember rm ON rm.gwatingRoom.id = g.id WHERE g.location LIKE :location AND rm.member.id NOT IN (SELECT b.blockMember.id FROM Block b WHERE b.blockItMember.email = :email)")
    List<GwatingCardDto> findAllByLocation(@Param("location") String location, @Param("email") String email);

    Optional<GwatingRoom> findById(@Param("id") Long id);

}
