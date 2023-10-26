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
    public String heart(Long id, String email) {
        Member sendMember = memberRepository.findMyMember(email);
        Optional<Member> receiverMemberOptional = memberRepository.findById(id);

        if (sendMember == null || receiverMemberOptional.isEmpty()) {
            return "존재하지 않는 회원입니다.";
        }

        Member receiverMember = receiverMemberOptional.get();

        if (heartRepository.countBySenderAndReceiver(sendMember, receiverMember) != 0) {
            return "이미 하트를 보낸 회원입니다.";
        }

        Heart heart = new Heart(sendMember, receiverMember);
        heartRepository.save(heart);

        // 받는 사람 알림창에 뜰 내용 DB에 저장
        Alert alert = Alert.builder()
                .receiverMember(receiverMember)
                .image(sendMember.getImage())
                .name(sendMember.getName())
                .message("나에게 하트를 눌렀어요! 마음에 드시나요?")
                .sendAt(LocalDate.now().toString())
                .isCheck(false)
                .build();

        alertRepository.save(alert);
        return "하트 보내기 성공";
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
