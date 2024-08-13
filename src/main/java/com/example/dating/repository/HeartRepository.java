package com.example.dating.repository;

import com.example.dating.domain.Heart;
import com.example.dating.domain.Member;
import com.example.dating.dto.heart.HeartMemberDto;
import com.example.dating.dto.member.MemberCommonDto;
import com.example.dating.dto.member.MemberInfoDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {
//    @Query("select new com.example.dating.dto.member.MemberCommonDto(m.id, m.birthDay, m.nickName, m.description, m.gender, m.address, " +
//            "m.age, m.height, m.image, m.personalInfo, m.personality, m.interest, m.likePersonality) " +
//            "from Heart h left join Member m on h.receiver = m where h.sender.email = :email order by rand()")
//    List<MemberCommonDto> findFiveRandomMemberBySender(@Param("email") String email, Pageable pageable);

//    @Query("select new com.example.dating.dto.member.MemberCommonDto(m.id, m.birthDay, m.nickName, m.description, m.gender, m.address, " +
//            "m.age, m.height, m.image, m.personalInfo, m.personality, m.interest, m.likePersonality) " +
//            "from Heart h left join Member m on h.sender = m where h.receiver.email = :email order by rand()")
//    List<MemberCommonDto> findFiveRandomMemberByReceiver(@Param("email") String email, Pageable pageable);

    @Query(value = "select m from Heart h left join Member m on h.sender = m where h.receiver.email = :email order by rand()")
    List<Member> findFiveRandomMemberByReceiver(@Param("email") String email, Pageable pageable);

    @Query(value = "select m from Heart h left join Member m on h.receiver = m where h.sender.email = :email order by rand()")
    List<Member> findFiveRandomMemberBySender(@Param("email") String email, Pageable pageable);

    Integer countBySenderAndReceiver(Member sender, Member receiver);
}
