package com.example.dating.repository;

import com.example.dating.domain.RoomMember;
import com.example.dating.dto.member.MemberInviteDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
    @Query("select new com.example.dating.dto.member.MemberInviteDto(m.id, m.nickName, m.age, m.address) from RoomMember r left join Member m on r.member.id = m.id")
    List<MemberInviteDto> findRoomMember();
}
