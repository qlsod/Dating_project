package com.example.dating.repository;

import com.example.dating.domain.ChatRoom;
import com.example.dating.domain.Member;
import com.example.dating.dto.chat.ChatListDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("select c from ChatRoom c where (c.member = :member and c.otherMember = :otherMember) or  (c.member = :otherMember and c.otherMember = :member)")
    Optional<ChatRoom> findChatRoom(@Param("member") Member member, @Param("otherMember") Member otherMember);

    @Query("select c.uuid from ChatRoom c where c.id = :id")
    String findChatRoomUUID(@Param("id") Long id);

    @Query("SELECT new com.example.dating.dto.chat.ChatListDto(c.id, c.otherMember.name, c.otherMember.image , MAX(cm.message), MAX(cm.createdAt)) FROM ChatRoom c JOIN ChatMessage cm ON c.id = cm.chatRoomId "
            + "WHERE c.member.email = :email AND c.type = :type AND (c.member.id = cm.memberId OR c.otherMember.id = cm.memberId) AND c.otherMember.id NOT IN (SELECT b.blockMember.id FROM Block b WHERE b.blockItMember.email = :email) " +
            "GROUP BY c.id, c.otherMember.name, c.member.image")
    List<ChatListDto> findListByMember(@Param("email") String email, @Param("type") String type);

    @Query("select new com.example.dating.dto.chat.ChatListDto(c.id, c.member.name, c.member.image, MAX(cm.message), MAX(cm.createdAt)) from ChatRoom c join ChatMessage cm on c.id = cm.chatRoomId "
            +"where c.otherMember.email = :email AND c.type = :type and (c.member.id = cm.memberId or c.otherMember.id = cm.memberId) AND c.member.id NOT IN (SELECT b.blockMember.id FROM Block b WHERE b.blockItMember.email = :email) " +
            "GROUP BY c.id, c.member.name, c.otherMember.image")
    List<ChatListDto> findListByOtherMember(@Param("email") String email, @Param("type") String type);

    Optional<ChatRoom> findChatRoomById(@Param("id") Long id);



}
