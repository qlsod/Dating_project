package com.example.dating.repository;

import com.example.dating.domain.HumanMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HumanMemberRepository extends JpaRepository<HumanMember, Long> {
    @Query("SELECT h FROM HumanMember h WHERE h.member.email = :email")
    Optional<HumanMember> findByEmail(@Param("email") String email);
}
