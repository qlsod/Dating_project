package com.example.dating.service;

import com.example.dating.domain.Member;
import com.example.dating.dto.MemberCardDto;
import com.example.dating.dto.MemberDto;
import com.example.dating.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void save(MemberDto memberDto) {
        Member member = new Member();
        member.createMember(memberDto);

        memberRepository.save(member);
    }

    @Transactional
    public List<MemberCardDto> getRandomMemberList() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        return memberRepository.findRandomMember(pageRequest);
    }
}
