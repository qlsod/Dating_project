package com.example.dating.repository;

import com.example.dating.domain.Heart;
import com.example.dating.dto.heart.HeartMemberDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    @Query("select new com.example.dating.dto.heart.HeartMemberDto(m.id, m.name, m.age) from Heart h left join Member m on h.receiver = m where h.sender.account.email = :email order by function('RAND')")
    List<HeartMemberDto> findFiveRandomMemberBySender(@Param("email") String email, Pageable pageable);

    @Query("select new com.example.dating.dto.heart.HeartMemberDto(m.id, m.name, m.age) from Heart h left join Member m on h.sender = m where h.receiver.account.email = :email order by function('RAND')")
    List<HeartMemberDto> findFiveRandomMemberByReceiver(@Param("email") String email, Pageable pageable);
}
