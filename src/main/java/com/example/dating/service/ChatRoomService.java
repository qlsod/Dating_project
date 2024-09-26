package com.example.dating.service;

import com.example.dating.domain.ChatRead;
import com.example.dating.domain.ChatRoom;
import com.example.dating.domain.Member;
import com.example.dating.dto.chat.ChatListDto;
import com.example.dating.dto.chat.ChatOneDto;
import com.example.dating.repository.ChatReadRepository;
import com.example.dating.repository.ChatRoomRepository;
import com.example.dating.repository.MemberRepository;
import com.example.dating.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final ChatReadRepository chatReadRepository;

    @Transactional
    public Long createRoom(String email, Long id, String type) {
        Member member = memberRepository.findByEmail(email).get();
        Member otherMember = memberRepository.findById(id).get();
//        String chatRoomId = UUID.randomUUID().toString();

        ChatRoom chatRoom = new ChatRoom(member, otherMember, type);
        chatRoomRepository.save(chatRoom);

        ChatRead chatRead = new ChatRead(chatRoom, member.getId(), true);
        ChatRead otherChatRead = new ChatRead(chatRoom, otherMember.getId(), false);
        chatReadRepository.save(chatRead);
        chatReadRepository.save(otherChatRead);
        return chatRoom.getId();
    }

    public void checkChatRoomExist(String email, Long id) {
        Member member = memberRepository.findByEmail(email).get();
        int check = chatRoomRepository.countChatRoomsByMemberEmail(member.getId(), id);
        log.info(String.valueOf(check));
        if (check > 0) {
            throw new RuntimeException("이미 채팅방이 존재합니다.");
        }
    }

    public List<ChatListDto> getList(String email, String type) {
        List<ChatListDto> listByMember = chatRoomRepository.findListByMember(email, type);
        List<ChatListDto> listByOtherMember = chatRoomRepository.findListByOtherMember(email, type);

        // 두 목록을 결합
        List<ChatListDto> combinedList = new ArrayList<>();
        combinedList.addAll(listByMember);
        combinedList.addAll(listByOtherMember);

        return combinedList;
    }

    @Transactional
    public void chatIsRead(String email, Long chatRoomId) {

        Member member = memberRepository.findByEmail(email).get();
        ChatRead chatRead = chatReadRepository.findByUserIdAndChatRoomId(chatRoomId, member.getId());
        chatRead.setIsRead(true);
        chatReadRepository.save(chatRead);
    }

    public List<ChatOneDto> getOne(String email, Long roomId) {
        List<ChatOneDto> messages;
        // email로 member 찾기
        Member member = memberRepository.findByEmail(email).get();

        // roomId로 chatRoom 정보 찾기
        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(roomId).get();

        if (chatRoom.getMember().equals(member)) {
            messages = messageRepository.findMessagesOtherMember(roomId);
        } else {
            messages = messageRepository.findMessagesMember(roomId);
        }

//        messages.forEach(message -> {
//            message.setMyId(member.getId());
//            if (message.getMyId().equals(message.getId())) {
//                message.setImage(null);
//            }
//        });

        return messages;
    }
}
