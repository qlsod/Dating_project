package com.example.dating.service;

import com.example.dating.domain.Member;
import com.example.dating.dto.Fcm.FcmSendDto;
import com.example.dating.redis.service.RedisService;
import com.example.dating.repository.MemberRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final FirebaseMessaging firebaseMessaging;
    private final RedisService redisService;
    private final MemberRepository memberRepository;


    public void sendPush(FcmSendDto fcmSendDto) {

        Optional<Member> member = memberRepository.findById(fcmSendDto.getTargetUserId());

        if (member.isPresent()) {
            String deviceToken = redisService.getValues(member.get().getEmail());
            if(deviceToken != null) {
                Notification notification =
                        Notification.builder()
                                .setTitle(fcmSendDto.getTitle())
                                .setBody(fcmSendDto.getBody())
                                .build();

                Message message = Message.builder()
                        .setToken(deviceToken)
                        .setNotification(notification)
                        .build();

                try {
                    firebaseMessaging.send(message);
                } catch (FirebaseMessagingException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException("서버에 저장된 해당 유저의 DeviceToken이 존재하지 않습니다.");
            }
        } else {
            throw new RuntimeException("해당 유저가 가입되지 않은 유저입니다.");
        }
    }

}
