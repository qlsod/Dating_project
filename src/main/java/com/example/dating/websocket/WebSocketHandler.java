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

            Long chatRoomId = chatMessageDto.getChatRoomId();
            Set<WebSocketSession> chatRoomSessions = chatRoomSessionMap.computeIfAbsent(String.valueOf(chatRoomId), k -> Collections.synchronizedSet(new HashSet<>()));

            if (chatMessageDto.getMessageType().equals(MessageType.TALK)) {
                if (session.isOpen()) {
                    chatRoomSessions.add(session);
                }

                chatService.chatCreate(chatMessageDto);

                try {
                    sendMessageToChatRoom(textMessage, chatRoomSessions);
                } catch (IllegalStateException e) {
                    removeClosedSession(chatRoomSessions, session);
                    sendMessageToChatRoom(textMessage, chatRoomSessions);
                }
            }

            if (chatMessageDto.getMessageType().equals(MessageType.QUIT)) {

                chatService.deleteChat(chatMessageDto);

                chatRoomSessions.remove(session);
                chatMessageDto.setMessage(chatMessageDto.getNickName() + "님이 퇴장했습니다.");
                sendMessageToChatRoom(new TextMessage(mapper.writeValueAsString(chatMessageDto)), chatRoomSessions);
                session.close();
            }
        }catch (Exception e) {
            log.info(String.valueOf(e));
        }
    }

    private void sendToEachSocket(Set<WebSocketSession> sessions, TextMessage message) {
        sessions.parallelStream().forEach(roomSession -> {
            try {
                roomSession.sendMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }



    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        sessions.remove(session);
        chatRoomSessionMap.values().forEach(sessions -> sessions.remove(session));

        // 해당 session이 속한 chatRoom을 찾는다.
        log.info("해당 session이 속한 chatRoom을 찾는다.");
        for (Map.Entry<String, Set<WebSocketSession>> entry : chatRoomSessionMap.entrySet()) {
            Set<WebSocketSession> chatRoomSessions = entry.getValue();
            chatRoomSessions.remove(session);  // 해당 session 제거

            // 채팅방에 사용자가 없는 경우 메시지 삭제
            log.info("채팅방에 사용자가 없는 경우 메시지 삭제");

            if (chatRoomSessions.isEmpty()) {
                Long chatRoomId = Long.parseLong(entry.getKey());

//                chatService.deleteChat(chatRoomId);

                // 채팅방 세션 맵에서도 제거
                chatRoomSessionMap.remove(chatRoomId);

                log.info("다 제거됨");
            }
        }
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
