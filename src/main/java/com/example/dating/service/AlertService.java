package com.example.dating.service;

import com.example.dating.domain.Alert;
import com.example.dating.domain.Member;
import com.example.dating.dto.alert.AlertDto;
import com.example.dating.dto.response.alert.AlertRes;
import com.example.dating.repository.AlertRepository;
import com.example.dating.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final MemberRepository memberRepository;



    @Transactional
    public List<AlertDto> getAlertList(String email) {

        Member member = memberRepository.findByEmail(email).get();

        List<AlertDto> alertList = alertRepository.findByReceiverMember(member.getId());

        return alertList;

    }



//    public long countNotCheckAlert(String email) {
//        List<Alert> alertList = getMyAlert(email);
//        return alertList.stream().filter(a -> !a.isChatExist()).count();
//    }
//
//    public void saveAlert(Member receiverMember, Member senderMember, String content, LocalDateTime sendAt, boolean isCheck) {
//        Alert alert = new Alert(receiverMember, senderMember, content, sendAt, isCheck);
//        alertRepository.save(alert);
//    }

//    private List<Alert> getMyAlert(String email) {
//        Member findMember = memberRepository.findByEmail(email).get();
//        return alertRepository.findByReceiverMemberOrderByIdDesc(findMember);
//    }
}
