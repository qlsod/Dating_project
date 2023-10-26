package com.example.dating.service;

import com.example.dating.domain.Alert;
import com.example.dating.domain.Member;
import com.example.dating.dto.alert.AlertDto;
import com.example.dating.repository.AlertRepository;
import com.example.dating.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public List<AlertDto> getAlertList(String email) {
        List<Alert> alertList = getMyAlert(email);
        alertList.forEach(Alert::check);

        return alertList.stream().map(a -> a.mapEntityToDto(a)).collect(Collectors.toList());
    }

    public long countNotCheckAlert(String email) {
        List<Alert> alertList = getMyAlert(email);
        return alertList.stream().filter(a -> !a.isCheck()).count();
    }

    private List<Alert> getMyAlert(String email) {
        Member myProfile = memberRepository.findMyMember(email);
        List<Alert> alertList = alertRepository.findByReceiverMemberOrderByIdDesc(myProfile);
        return alertList;
    }
}
