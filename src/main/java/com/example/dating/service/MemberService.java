package com.example.dating.service;

import com.example.dating.domain.Account;
import com.example.dating.domain.Member;
import com.example.dating.dto.member.MemberCardDto;
import com.example.dating.dto.member.MemberGenderDto;
import com.example.dating.dto.member.MemberInfoDto;
import com.example.dating.dto.member.MemberMbtiDto;
import com.example.dating.mbti.Mbti;
import com.example.dating.repository.AccountRepository;
import com.example.dating.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;

    /**
     * 입력한 회원 정보 저장
     */
    @Transactional
    public void save(String email, MemberInfoDto memberInfoDto) {
        Account account = accountRepository.findByEmail(email).get();
        Member member = new Member();
        member.createMember(memberInfoDto, account);

        memberRepository.save(member);
    }

    /**
     * 20명의 랜덤 이성 회원을 추천
     */
    public List<MemberCardDto> getRandomMemberList(String email) {
        MemberGenderDto memberGenderDto = memberRepository.findMyGender(email);

        PageRequest pageRequest = PageRequest.of(0, 20);
        return memberRepository.findRandomMember(memberGenderDto.getId(), memberGenderDto.getGender(), pageRequest);
    }

    /**
     * 나와 잘 맞는 mbti를 가진 5명의 이성 회원을 추천
     */
    public List<MemberMbtiDto> getGoodMbtiList(String email, List<MemberCardDto> randomMemberList) {
        List<Long> randomMemberIdList = randomMemberList.stream().map(MemberCardDto::getId).collect(Collectors.toList());
        MemberMbtiDto memberMbtiDto = memberRepository.findMyMbti(email);
        List<String> partnerMbtiList = Mbti.getGoodPartner(memberMbtiDto.getMbti());

        PageRequest pageRequest = PageRequest.of(0, 5);
        return memberRepository.findRandomMemberbyMbtiList(partnerMbtiList, randomMemberIdList, memberMbtiDto.getId(), pageRequest);

    }
}
