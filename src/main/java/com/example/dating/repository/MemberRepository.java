package com.example.dating.repository;

import com.example.dating.domain.Member;
import com.example.dating.dto.member.MemberCardDto;
import com.example.dating.dto.member.MemberGenderDto;
import com.example.dating.dto.member.MemberMbtiDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select m from Account a left join Member m on a.id = m.account.id where a.email = :email")
    Member findMyMember(@Param("email") String email);

    @Query("select new com.example.dating.dto.member.MemberGenderDto(m.id, m.gender) from Member m where m.account.id in (select a.id from Account a where a.email = :email)")
    MemberGenderDto findMyGender(@Param("email") String email);

    @Query("select new com.example.dating.dto.member.MemberCardDto(m.id, m.name, m.residence, m.age, m.height, m.image) from Member m " +
            "where not m.gender = :gender and m.id not in (select h.receiver.id from Heart h where h.sender.id = :id) order by rand()")
    List<MemberCardDto> findRandomMember(@Param("id") Long id, @Param("gender") String gender, Pageable pageable);

    @Query("select new com.example.dating.dto.member.MemberMbtiDto(m.id, m.mbti) from Member m where m.account.id = (select a.id from Account a where a.email = :email)")
    MemberMbtiDto findMyMbti(@Param("email") String email);

    @Query("select new com.example.dating.dto.member.MemberMbtiDto(m.id, m.name, m.mbti) from Member m " +
            "where m.mbti in :mbtiList and not m.id = :id and m.id not in (select sm.id from Member sm where sm.id in :randomMemberIdList) order by rand()")
    List<MemberMbtiDto> findRandomMemberbyMbtiList(@Param("mbtiList") List<String> mbtiList,
                                                   @Param("randomMemberIdList") List<Long> randomMemberIdList,
                                                   @Param("id") Long id,
                                                   Pageable pageable);
}