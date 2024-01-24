package com.example.dating.repository;

import com.example.dating.domain.ChatMessage;
import com.example.dating.dto.chat.ChatOneDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface MessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT new com.example.dating.dto.chat.ChatOneDto(ch.memberId, c.otherMember.image, ch.message, ch.createdAt) FROM ChatMessage ch join ChatRoom c on ch.chatRoomId = c.id WHERE ch.chatRoomId = :roomId ORDER BY ch.createdAt")
    List<ChatOneDto> findMessagesOtherMember(@Param("roomId") Long roomId);

    @Query("SELECT new com.example.dating.dto.chat.ChatOneDto(ch.memberId, c.member.image, ch.message, ch.createdAt) FROM ChatMessage ch join ChatRoom c on ch.chatRoomId = c.id WHERE ch.chatRoomId = :roomId ORDER BY ch.createdAt")
    List<ChatOneDto> findMessagesMember(@Param("roomId") Long roomId);
}

