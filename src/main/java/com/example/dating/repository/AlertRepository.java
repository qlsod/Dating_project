package com.example.dating.repository;

import com.example.dating.domain.Alert;
import com.example.dating.domain.Member;
import com.example.dating.dto.alert.AlertDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    @Query("select a.senderMember.image, a.content, a.createdAt, a.chatExist from Alert a where a.receiverMember.id = :id")
    List<AlertDto> findByReceiverMember(@Param("id") Long id);


    @Query("select count(c) from ChatRoom c " +
            "where (c.member.id = :id and c.otherMember.id = :otherId) " +
            "or (c.member.id = :otherId and c.otherMember.id = :id)")
    int countChatRoomsByMemberEmail(@Param("id") Long id, @Param("otherId") Long otherId);
}