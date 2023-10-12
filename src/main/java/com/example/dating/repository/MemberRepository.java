package com.example.dating.repository;

import com.example.dating.domain.Member;
import com.example.dating.dto.MemberCardDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select m.gender from Account a left join Member m on a.id = m.account.id where a.email = :email")
    String findMyGender(@Param("email") String email);

    @Query(value = "select new com.example.dating.dto.MemberCardDto(m.id, m.name, m.residence, m.age, m.height, m.image) from Member m where not m.gender = :gender order by function('RAND')")
    List<MemberCardDto> findRandomMember(Pageable pageable, @Param("gender") String gender);
}
