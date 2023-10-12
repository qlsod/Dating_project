package com.example.dating.service;

import com.example.dating.domain.Account;
import com.example.dating.domain.Member;
import com.example.dating.dto.MemberCardDto;
import com.example.dating.dto.MemberInfoDto;
import com.example.dating.repository.AccountRepository;
import com.example.dating.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Transactional
    public List<MemberCardDto> getRandomMemberList(String email) {
        String myGender = memberRepository.findMyGender(email);

        PageRequest pageRequest = PageRequest.of(0, 20);
        return memberRepository.findRandomMember(pageRequest, myGender);
    }
}
