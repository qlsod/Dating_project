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
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

import static com.example.dating.dto.chat.ChatMessageDto.*;

@Slf4j
@Component
@Getter
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;

    // WebSocketSession - WebSocket 연결을 나타내는 객체
    // 현재 열려 있는 모든 WebSocket 세션들을 저장
    private final Set<WebSocketSession> sessions = new HashSet<>();

    // 채팅 방에 대한 정보를 저장하고 각 채팅 방에 속한 WebSocket 세션들의 집합을 관리. 채팅방 ID: { session1, session2 }
    private final Map<String, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connect Success");
    }

    // 소켓 통신 시 메세지의 전송을 다루는 부분
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        // 받은 메시지의 내용을 가져옴
        String payload = message.getPayload();

        // 메시지 내용을 Java 객체로 변환
        ChatMessageDto chatMessageDto = mapper.readValue(payload, ChatMessageDto.class);

        // ChatMessageDto 객체를 JSON 형식으로 변환
        String jsonMessage = mapper.writeValueAsString(chatMessageDto);
        TextMessage textMessage = new TextMessage(jsonMessage);

        // 현재 세션이 세션 Set 에 없으면 추가
        sessions.add(session);
//        session.sendMessage(textMessage);
        System.out.println(session);

        String chatRoomUUID = chatRoomRepository.findChatRoomUUID(chatMessageDto.getChatRoomId());

        // 해당 채팅방에 세션들을 가져옴
        Set<WebSocketSession> chatRoomSessions = chatRoomSessionMap.computeIfAbsent(chatRoomUUID, k -> new HashSet<>());
        System.out.println(chatRoomSessionMap.get(chatRoomUUID));

        // 만약 채팅 메시지 타입이 ENTER 면 해당 채팅방에 세션 추가
//        if (chatMessageDto.getMessageType().equals(MessageType.ENTER)) {
//            chatRoomSessions.add(session);
//        }

        // 채팅 메세지 타입이 TALK 이면 해당 채팅방에 있는 세션들로 메시지 전송
        if (chatMessageDto.getMessageType().equals(MessageType.TALK)) {
            chatRoomSessions.add(session);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.mapToEntity(chatMessageDto);
            messageRepository.save(chatMessage);
            try {
//                sendMessage(session, jsonMessage);
                sendMessageToChatRoom(textMessage, chatRoomSessions);
            } catch (IllegalStateException e) {
                log.error(e.getMessage(), e);
                removeClosedSession(sessions, session);
                sendMessageToChatRoom(textMessage, chatRoomSessions);
            }
        }

        // 채팅 메시지 타입이 QUIT 이면 해당 채팅방 세션에서 제거
        if (chatMessageDto.getMessageType().equals(MessageType.QUIT)) {
            System.out.println(111);
            chatRoomSessions.remove(session);
            session.close();
        }
    }

    // 채팅방에 포함된 세션 중 현재 열려있지 않는 세션은 제거하는 메서드
    private void removeClosedSession(Set<WebSocketSession> sessions, WebSocketSession session) {
        sessions.removeIf(s -> !session.isOpen());
    }

    // 특정 채팅방의 모든 WebSocket 세션에 메시지를 전송하는 메서드
    private void sendMessageToChatRoom(TextMessage textMessage, Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.parallelStream().forEach(sess -> sendMessage(sess, textMessage.getPayload()));
    }

    // 메시지 전송 메서드
    public void sendMessage(WebSocketSession session, String message) {
        try {
            session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
