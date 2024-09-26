package com.example.dating.websocket;

import com.example.dating.domain.ChatMessage;
import com.example.dating.domain.ChatRead;
import com.example.dating.domain.ChatRoom;
import com.example.dating.domain.Member;
import com.example.dating.dto.chat.ChatMessageDto;
import com.example.dating.repository.ChatReadRepository;
import com.example.dating.repository.ChatRoomRepository;
import com.example.dating.repository.MessageRepository;
import com.example.dating.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

import static com.example.dating.dto.chat.ChatMessageDto.MessageType;

@Slf4j
@Component
@Getter
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;

    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    private final Map<String, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;
    private final ChatReadRepository chatReadRepository;
    private final MessageRepository messageRepository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String payload = message.getPayload();
            ChatMessageDto chatMessageDto = mapper.readValue(payload, ChatMessageDto.class);
            String jsonMessage = mapper.writeValueAsString(chatMessageDto);
            TextMessage textMessage = new TextMessage(jsonMessage);

            String chatRoomUUID = chatRoomRepository.findChatRoomUUID(chatMessageDto.getChatRoomId());
            Set<WebSocketSession> chatRoomSessions = chatRoomSessionMap.computeIfAbsent(chatRoomUUID, k -> Collections.synchronizedSet(new HashSet<>()));

            if (chatMessageDto.getMessageType().equals(MessageType.TALK)) {
                if (session.isOpen()) {
                    chatRoomSessions.add(session);
                }

                chatService.chatCreate(chatMessageDto);

//                ChatMessage chatMessage = new ChatMessage();
//                chatMessage.mapToEntity(chatMessageDto);
//                messageRepository.save(chatMessage);
//
//                log.info("11");
//                // 해당 채팅을 상대방이 읽지 않은 것으로 처리
//                ChatRoom chatRoom = chatRoomRepository.findAllByChatRoomId(chatMessage.getChatRoomId());
//                if (chatRoom == null) {
//                    log.error("ChatRoom not found for id: " + chatMessage.getChatRoomId());
//                    return; // 또는 적절한 예외를 던짐
//                }
//                log.info("22");
//
//                // 보낸 유저의 닉네임이 Member일경우 OtherMember를, OtherMember일 경우 Member를 고침
//                ChatRead chatRead = (chatRoom.getMember().getNickName().equals(chatMessage.getNickName())) ?
//                        chatReadRepository.findByUserIdAndChatRoomId(chatRoom.getId(), chatRoom.getOtherMember().getId()) :
//                        chatReadRepository.findByUserIdAndChatRoomId(chatRoom.getId(), chatRoom.getMember().getId());
//
//                log.info("33");
//
//                chatRead.setIsRead(false);
//                log.info("44");
//
//                chatReadRepository.save(chatRead);
//
//                log.info("55");

                try {
                    sendMessageToChatRoom(textMessage, chatRoomSessions);
                } catch (IllegalStateException e) {
                    removeClosedSession(chatRoomSessions, session);
                    sendMessageToChatRoom(textMessage, chatRoomSessions);
                }
            }

            if (chatMessageDto.getMessageType().equals(MessageType.QUIT)) {
                chatRoomSessions.remove(session);
                session.close();
            }
        }catch (Exception e) {
            log.info(String.valueOf(e));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        chatRoomSessionMap.values().forEach(sessions -> sessions.remove(session));
    }

    private void removeClosedSession(Set<WebSocketSession> sessions, WebSocketSession session) {
        sessions.removeIf(s -> !s.isOpen());
    }

    private void sendMessageToChatRoom(TextMessage textMessage, Set<WebSocketSession> chatRoomSessions) {
        for (WebSocketSession sess : new HashSet<>(chatRoomSessions)) {
            if (sess.isOpen()) {
                sendMessage(sess, textMessage.getPayload());
            } else {
                log.warn("Removing closed session from chat room: " + sess.getId());
                chatRoomSessions.remove(sess);
            }
        }
    }

    public void sendMessage(WebSocketSession session, String message) {
        if (session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        } else {
            log.warn("Attempted to send message to closed session: " + session.getId());
        }
    }
}
