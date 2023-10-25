package com.example.dating.service;

import com.example.dating.domain.Member;
import com.example.dating.dto.alert.AlertDto;
import com.example.dating.repository.AlertRepository;
import com.example.dating.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final MemberRepository memberRepository;

    public List<AlertDto> getAlert(String email) {
        Member myProfile = memberRepository.findMyMember(email);
        return alertRepository.findByReceiverMember(myProfile);
    }
}
