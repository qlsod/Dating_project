package com.example.dating.repository;

import com.example.dating.domain.Member;
import com.example.dating.dto.MemberCardDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query(value = "select new com.example.dating.dto.MemberCardDto(m.id, m.name, m.residence, m.age, m.height, m.image) from Member m order by function('RAND')")
    List<MemberCardDto> findRandomMember(Pageable pageable);
}
