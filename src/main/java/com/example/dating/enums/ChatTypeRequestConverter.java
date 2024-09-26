package com.example.dating.enums;

import org.springframework.core.convert.converter.Converter;

public class ChatTypeRequestConverter implements Converter<String, ChatType> {

    @Override
    public ChatType convert(String requestCategory) {
        return ChatType.create(requestCategory.toUpperCase());
    }

}
