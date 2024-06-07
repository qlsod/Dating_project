package com.example.dating.websocket;

import com.example.dating.domain.ChatMessage;
import com.example.dating.dto.chat.ChatMessageDto;
import com.example.dating.repository.ChatRoomRepository;
import com.example.dating.repository.MessageRepository;
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
    private final MessageRepository messageRepository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connect Success: " + session.getId());
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
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

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.mapToEntity(chatMessageDto);
            messageRepository.save(chatMessage);

            try {
                sendMessageToChatRoom(textMessage, chatRoomSessions);
            } catch (IllegalStateException e) {
                log.error(e.getMessage(), e);
                removeClosedSession(chatRoomSessions, session);
                sendMessageToChatRoom(textMessage, chatRoomSessions);
            }
        }

        if (chatMessageDto.getMessageType().equals(MessageType.QUIT)) {
            chatRoomSessions.remove(session);
            session.close();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("Connection closed: " + session.getId());
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
