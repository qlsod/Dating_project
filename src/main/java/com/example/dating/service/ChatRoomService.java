package com.example.dating.service;

import com.example.dating.domain.ChatRoom;
import com.example.dating.domain.Member;
import com.example.dating.dto.chat.ChatListDto;
import com.example.dating.dto.chat.ChatOneDto;
import com.example.dating.repository.ChatRoomRepository;
import com.example.dating.repository.MemberRepository;
import com.example.dating.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    @Transactional
    public Long createRoom(String email, Long id, String type) {
        Member member = memberRepository.findByEmail(email).get();
        Member otherMember = memberRepository.findById(id).get();
        String chatRoomId = UUID.randomUUID().toString();

        ChatRoom chatRoom = new ChatRoom(member, otherMember, chatRoomId, type);
        chatRoomRepository.save(chatRoom);
        return chatRoom.getId();
    }

    public List<ChatListDto> getList(String email, String type) {
        List<ChatListDto> listByMember = chatRoomRepository.findListByMember(email, type);
        List<ChatListDto> listByOtherMember = chatRoomRepository.findListByOtherMember(email, type);
//        listByMember.addAll(listByOtherMember);
//        return listByMember;
        // 두 목록을 결합
        List<ChatListDto> combinedList = new ArrayList<>();
        combinedList.addAll(listByMember);
        combinedList.addAll(listByOtherMember);

        return combinedList;
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
