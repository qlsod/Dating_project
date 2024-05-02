package com.example.dating.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private String code;

    // 랜덤 6자리 인증 번호 생성
    private void createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstance("SHA1PRNG");
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            code = builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Mail createCode Error");
        }
    }

    // 메일 양식 작성
    public MimeMessage createEmailForm(String email) throws MessagingException {

        createCode();//인증 코드 생성
        String setFrom = "cammeet3824@gmail.com"; // 보내는 사람
        String title = "[캠밋] 인증번호를 안내해드립니다."; // 제목

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email)); // 보낼 이메일 설정
        message.setSubject(title); // 제목 설정
        message.setFrom(setFrom); // 보내는 이메일
        message.setText(setContext(code), "utf-8", "html");

        return message;
    }

    // 실제 메일 전송
    public String sendEmail(String toEmail) throws MessagingException, UnsupportedEncodingException {
        MimeMessage emailForm = createEmailForm(toEmail);
        emailSender.send(emailForm);

        return code;
    }

    // 타임리프를 이용한 context 설정
    public String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("mail", context); //mail.html
    }
}
