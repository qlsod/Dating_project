package com.example.dating.repository;

import com.example.dating.domain.ChatRoom;
import com.example.dating.domain.Member;
import com.example.dating.dto.chat.ChatListDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("select c.otherMember from ChatRoom c where id = :id")
    Member findListOtherMember(@Param("id") Long id);

    @Query("select c from ChatRoom c where id = :id")
    ChatRoom findAllByChatRoomId(@Param("id") Long id);
    @Query("select count(c) from ChatRoom c " +
            "where (c.member.id = :id and c.otherMember.id = :otherId) " +
            "or (c.member.id = :otherId and c.otherMember.id = :id)")
    int countChatRoomsByMemberEmail(@Param("id") Long id, @Param("otherId") Long otherId);

    @Query("select c.uuid from ChatRoom c where c.id = :id")
    String findChatRoomUUID(@Param("id") Long id);

    @Query("SELECT new com.example.dating.dto.chat.ChatListDto(c.id, c.otherMember.nickName, c.otherMember.image, cm.message, cm.createdAt, COALESCE(cr.isRead, false)) " +
            "FROM ChatRoom c " +
            "JOIN ChatMessage cm ON c.id = cm.chatRoomId " +
            "LEFT JOIN ChatRead cr ON c.id = cr.chatRoom.id AND cr.userId = (SELECT m.id FROM Member m WHERE m.email = :email) " +
            "WHERE cm.createdAt = (SELECT MAX(cm2.createdAt) FROM ChatMessage cm2 WHERE cm2.chatRoomId = c.id) " +
            "AND c.member.email = :email " +
            "AND c.type = :type " +
            "AND (c.member.nickName = cm.nickName OR c.otherMember.nickName = cm.nickName) " +
            "AND c.otherMember.id NOT IN (SELECT b.blockMember.id FROM Block b WHERE b.blockItMember.email = :email) " +
            "GROUP BY c.id, c.otherMember.nickName, c.otherMember.image, cm.message, cm.createdAt, cr.isRead " +
            "ORDER BY cm.createdAt DESC")
    List<ChatListDto> findListByMember(@Param("email") String email, @Param("type") String type);


    @Query("SELECT new com.example.dating.dto.chat.ChatListDto(c.id, c.member.nickName, c.member.image, cm.message, cm.createdAt, COALESCE(cr.isRead, false)) " +
            "FROM ChatRoom c " +
            "JOIN ChatMessage cm ON c.id = cm.chatRoomId " +
            "LEFT JOIN ChatRead cr ON c.id = cr.chatRoom.id AND cr.userId = (SELECT m.id FROM Member m WHERE m.email = :email) " +
            "WHERE cm.createdAt = (SELECT MAX(cm2.createdAt) FROM ChatMessage cm2 WHERE cm2.chatRoomId = c.id) " +
            "AND c.otherMember.email = :email " +
            "AND c.type = :type " +
            "AND (c.member.nickName = cm.nickName OR c.otherMember.nickName = cm.nickName) " +
            "AND c.member.id NOT IN (SELECT b.blockMember.id FROM Block b WHERE b.blockItMember.email = :email) " +
            "GROUP BY c.id, c.member.nickName, c.member.image, cm.message, cm.createdAt, cr.isRead " +
            "ORDER BY cm.createdAt DESC")
    List<ChatListDto> findListByOtherMember(@Param("email") String email, @Param("type") String type);



    Optional<ChatRoom> findChatRoomById(@Param("id") Long id);


    @Modifying
    @Query("DELETE FROM ChatRoom c WHERE c.member = :member OR c.otherMember = :member")
    void deleteByEmail(Member member);

}
