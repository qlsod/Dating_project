package com.example.dating.service;

import com.example.dating.domain.ChatMessage;
import com.example.dating.domain.ChatRead;
import com.example.dating.domain.ChatRoom;
import com.example.dating.domain.Member;
import com.example.dating.dto.chat.ChatMessageDto;
import com.example.dating.dto.fcm.FcmSendDto;
import com.example.dating.repository.ChatReadRepository;
import com.example.dating.repository.ChatRoomRepository;
import com.example.dating.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatReadRepository chatReadRepository;
    private final FcmService fcmService;

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
    public void deleteChat(ChatMessageDto chatMessageDto) {
        log.info("1");
        int isMember = chatRoomRepository.checkMemberByNickName(chatMessageDto.getChatRoomId(), chatMessageDto.getNickName());
        log.info("11111");

        Member member = (isMember == 0) ? chatRoomRepository.findOtherMember(chatMessageDto.getChatRoomId()) :
                chatRoomRepository.findMember(chatMessageDto.getChatRoomId());
        log.info("2");

        FcmSendDto fcmSendDto = new FcmSendDto(member.getNickName(), member.getNickName(),
            chatMessageDto.getMessage(), chatMessageDto.getChatRoomId());
        log.info("3");

        fcmService.sendPush(fcmSendDto);
        log.info("4");

        messageRepository.deleteByChatRoomId(chatMessageDto.getChatRoomId());
        chatReadRepository.deleteByChatRoomId(chatMessageDto.getChatRoomId());
        chatRoomRepository.deleteById(chatMessageDto.getChatRoomId());

    }

//    @Transactional
//    public void deleteChatOne(ChatMessageDto chatMessageDto) {
//        int isMember = chatRoomRepository.checkMemberByNickName(chatMessageDto.getChatRoomId(), chatMessageDto.getNickName());
//
//        if (isMember > 0 ) {
//            chatRoomRepository.deleteOtherMember(chatMessageDto.getChatRoomId());
//        } else {
//            chatRoomRepository.deleteMember(chatMessageDto.getChatRoomId());
//        }
//
//    }




}
