package com.example.dating.repository;

import com.example.dating.domain.ChatRead;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatReadRepository extends JpaRepository<ChatRead, Long> {

    @Query("select c from ChatRead c where c.chatRoom.id = :chatRoomId and c.userId = :userId")
    ChatRead findByUserIdAndChatRoomId(@Param("chatRoomId") Long chatRoomId, @Param("userId") Long userId);

}
