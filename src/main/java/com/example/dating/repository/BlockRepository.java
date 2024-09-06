package com.example.dating.repository;

import com.example.dating.domain.Block;
import com.example.dating.domain.Member;
import com.example.dating.dto.block.BlockListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlockRepository extends JpaRepository<Block, Long> {
    @Query("SELECT b.blockMember FROM Block b WHERE b.blockItMember.email = :email")
    List<Member> findByEmail(@Param("email") String email);


    @Modifying
    @Query("DELETE FROM Block b WHERE b.blockItMember.id = :myId AND b.blockMember.id = :id")
    void deleteBlockMemberById(@Param("myId") Long myId, @Param("id") Long id);
}
