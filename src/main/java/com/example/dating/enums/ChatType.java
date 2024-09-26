package com.example.dating.enums;

public enum ChatType {
    DM, MEETING;

    public static ChatType create(String requestChatType) {
        for (ChatType value : ChatType.values()) {
            if (value.toString().equals(requestChatType)) {
                return value;
            }
        }
        throw new IllegalStateException("일치하는 채팅방 종류가 존재하지 않습니다.");
    }
}
