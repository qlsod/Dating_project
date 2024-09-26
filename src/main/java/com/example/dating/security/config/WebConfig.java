package com.example.dating.security.config;

import com.example.dating.controller.ChatController;
import com.example.dating.enums.ChatTypeRequestConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ChatTypeRequestConverter());
    }

}
