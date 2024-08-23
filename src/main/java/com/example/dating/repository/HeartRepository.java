package com.example.dating.repository;

import com.example.dating.domain.Heart;
import com.example.dating.domain.Member;
import org.springframework.data.domain.PageRequest;
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

    @Query("select m from Heart h left join Member m on h.sender = m where h.receiver.email = :email order by h.id desc")
    List<Member> findByReceiver(@Param("email") String email, PageRequest pageRequest);

    @Query("select h.sender from Heart h left join Member m on h.sender = m " +
            "where h.receiver.email = :email " +
            "and h.id < (select h2.id from Heart h2 where h2.sender.id = :id) order by h.id desc")
    List<Member> findPagingMemberByReceiver(@Param("email") String email, @Param("id") Long id, PageRequest pageRequest);


    @Query("select h.receiver from Heart h left join Member m on h.sender = m " +
            "where h.sender.email = :email " +
            "and h.id < (select h2.id from Heart h2 where h2.receiver.id = :id) order by h.id desc")
    List<Member> findPagingMemberBySender(@Param("email") String email, @Param("id") Long id, PageRequest pageRequest);
    @Query(value = "select m from Heart h left join Member m on h.receiver = m where h.sender.email = :email order by h.id desc")
    List<Member> findMemberBySender(@Param("email") String email, Pageable pageable);

    Integer countBySenderAndReceiver(Member sender, Member receiver);
}
