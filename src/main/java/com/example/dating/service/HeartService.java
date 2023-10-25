package com.example.dating.service;

import com.example.dating.domain.Alert;
import com.example.dating.domain.Heart;
import com.example.dating.domain.Member;
import com.example.dating.dto.heart.HeartMemberDto;
import com.example.dating.repository.AlertRepository;
import com.example.dating.repository.HeartRepository;
import com.example.dating.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;
    private final AlertRepository alertRepository;

    /**
     * 하트를 보낸 사람과 받은 사람을 저장
     */
    @Transactional
    public void heart(Long id, String email) throws Exception {
        Member sendMember = memberRepository.findMyMember(email);
        Optional<Member> receiverMemberOptional = memberRepository.findById(id);

        if (sendMember != null && receiverMemberOptional.isPresent()) {
            Member receiverMember = receiverMemberOptional.get();
            Heart heart = new Heart(sendMember, receiverMember);
            heartRepository.save(heart);

            /*
            푸쉬 알림 보내는 로직 필요
             */

            // 받는 사람 알림창에 뜰 내용 DB에 저장
            Alert alert = Alert.builder()
                    .receiverMember(receiverMember)
                    .image(sendMember.getImage())
                    .name(sendMember.getName())
                    .message("나에게 하트를 눌렀어요! 마음에 드시나요?")
                    .sendAt(LocalDate.now().toString())
                    .build();

            alertRepository.save(alert);
        } else {
            throw new Exception();
        }
    }

    /**
     * 내가 하트를 보낸 리스트에서 랜덤 5명 조회
     */
    public List<HeartMemberDto> sendHeartList(String email) {
        PageRequest pageable = PageRequest.of(0, 5);
        return heartRepository.findFiveRandomMemberBySender(email, pageable);
    }

    /**
     * 내가 하트를 받은 리스트에서 랜덤 5명 조회
     */
    public List<HeartMemberDto> receiverHeartList(String email) {
        PageRequest pageable = PageRequest.of(0, 5);
        return heartRepository.findFiveRandomMemberByReceiver(email, pageable);
    }
}
