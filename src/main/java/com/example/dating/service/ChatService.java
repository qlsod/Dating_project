package com.example.dating.service;

import com.example.dating.domain.ChatMessage;
import com.example.dating.domain.ChatRead;
import com.example.dating.domain.ChatRoom;
import com.example.dating.dto.chat.ChatMessageDto;
import com.example.dating.repository.ChatReadRepository;
import com.example.dating.repository.ChatRoomRepository;
import com.example.dating.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatReadRepository chatReadRepository;

    @Transactional
    public void chatCreate(ChatMessageDto chatMessageDto) {

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.mapToEntity(chatMessageDto);
        messageRepository.save(chatMessage);

        // 해당 채팅을 상대방이 읽지 않은 것으로 처리
        ChatRoom chatRoom = chatRoomRepository.findAllByChatRoomId(chatMessage.getChatRoomId());

        // 보낸 유저의 닉네임이 Member일경우 OtherMember를, OtherMember일 경우 Member를 고침
        ChatRead chatRead = (chatRoom.getMember().getNickName().equals(chatMessage.getNickName())) ?
                chatReadRepository.findByUserIdAndChatRoomId(chatRoom.getId(), chatRoom.getOtherMember().getId()) :
                chatReadRepository.findByUserIdAndChatRoomId(chatRoom.getId(), chatRoom.getMember().getId());

        chatRead.setIsRead(false);

        chatReadRepository.save(chatRead);
    }


    @Transactional
    public void deleteChat(Long chatRoomId) {
        messageRepository.deleteByChatRoomId(chatRoomId);
        chatReadRepository.deleteByChatRoomId(chatRoomId);
        chatRoomRepository.deleteById(chatRoomId);
    }




}
