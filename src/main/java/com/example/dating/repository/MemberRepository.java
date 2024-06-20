package com.example.dating.repository;

import com.example.dating.domain.Member;
import com.example.dating.domain.Search;
import com.example.dating.dto.member.MemberCardDto;
import com.example.dating.dto.member.MemberInviteDto;
import com.example.dating.dto.member.MemberMbtiDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    void deleteByEmail(String email);

    @Query("select new com.example.dating.dto.member.MemberInviteDto(m.id, m.nickName, m.age, m.address) from Member m where not m.email = :email and m.id not in (SELECT b.blockMember.id FROM Block b WHERE b.blockItMember.email = :email)")
    List<MemberInviteDto> findAllNotContainMe(@Param("email") String email);

    @Query("select m from Member m WHERE m.email = :email")
    Member findIdByEmail(@Param("email") String email);

    @Query("select m from Member m WHERE m.nickName = :nickName")
    Member findByNickName(@Param("nickName") String nickName);

    @Query("select m from Member m where m.id in :inviteIdList")
    List<Member> findInviteMember(@Param("inviteIdList") List<Long> inviteIdList);

    @Query("select new com.example.dating.dto.member.MemberCardDto(m.id, m.nickName, m.address, m.age, m.height, m.image) " +
            "from Heart h left join Member m on h.receiver = m where h.sender.email = :email and m.id not in (SELECT b.blockMember.id FROM Block b WHERE b.blockItMember.email = :email)")
    List<MemberCardDto> findSendHeartList(@Param("email") String email);

    @Query("select new com.example.dating.dto.member.MemberCardDto(m.id, m.nickName, m.address, m.age, m.height, m.image) " +
            "from Heart h left join Member m on h.sender = m where h.receiver.email = :email and m.id not in (select hm.member.id from HumanMember hm) and m.id not in (SELECT b.blockMember.id FROM Block b WHERE b.blockItMember.email = :email)")
    List<MemberCardDto> findReceiverHeartList(@Param("email") String email);

    @Query("select new com.example.dating.dto.member.MemberCardDto(m.id, m.nickName, m.address, m.age, m.height, m.image) from Member m " +
            "where not m.gender = :gender and m.id not in (select h.receiver.id from Heart h where h.sender.id = :id) " +
            "and m.id not in (select hm.member.id from HumanMember hm) and m.id not in (SELECT b.blockMember.id FROM Block b WHERE b.blockItMember.id = :id) order by rand()")
    List<MemberCardDto> findRandomMember(@Param("id") Long id, @Param("gender") String gender, Pageable pageable);

//    @Query("select new com.example.dating.dto.member.MemberMbtiDto(m.id, m.nickName, m.mbti, m.comment) from Member m " +
//            "where m.mbti in :mbtiList and not m.id = :id and m.id not in (select sm.id from Member sm where sm.id in :randomMemberIdList) " +
//            "and m.id not in (select hm.member.id from HumanMember hm) and m.id not in (SELECT b.blockMember.id FROM Block b WHERE b.blockItMember.id = :id) order by rand()")
//    List<MemberMbtiDto> findRandomMemberbyMbtiList(@Param("mbtiList") List<String> mbtiList,
//                                                   @Param("randomMemberIdList") List<Long> randomMemberIdList,
//                                                   @Param("id") Long id,
//                                                   Pageable pageable);

    @Query(value = "SELECT m FROM Member m WHERE NOT m.gender = :gender")
    List<Member> findRandomRecommendMemberList(@Param("gender") String gender, Pageable pageable);
}