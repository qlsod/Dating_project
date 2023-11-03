package com.example.dating.service;

import com.example.dating.domain.Member;
import com.example.dating.dto.member.*;
import com.example.dating.mbti.Mbti;
import com.example.dating.repository.MemberRepository;
import com.example.dating.security.jwt.TokenInfo;
import com.example.dating.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public Long join(MemberJoinDto memberJoinDto) throws RuntimeException {
        // 이메일로 회원 찾기
        Optional<Member> memberOptional = memberRepository.findByEmail(memberJoinDto.getEmail());
        if (memberOptional.isPresent()) {
            throw new RuntimeException("이미 존재하는 회원입니다.");
        }

        String encodePassword = passwordEncoder.encode(memberJoinDto.getPassword());

        Member member = new Member(memberJoinDto.getEmail(), encodePassword);
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    public TokenInfo login(MemberJoinDto memberJoinDto) throws Exception {
        Optional<Member> memberOptional = memberRepository.findByEmail(memberJoinDto.getEmail());
        if (memberOptional.isEmpty()) {
            throw new Exception("회원이 존재하지 않습니다.");
        }

        Member member = memberOptional.get();

        // 복호화한 패스워드와 입력한 패스워드가 다르면 틀린 비밀번호
        if (!passwordEncoder.matches(memberJoinDto.getPassword(), member.getPassword())) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        // jwt 발급
        Authentication token = new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword());
        return tokenProvider.generateToken(token);
    }

    /**
     * 입력한 회원 정보 저장
     */
    @Transactional
    public void save(Long memberId, MemberInfoDto memberInfoDto) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);

       memberOptional.ifPresentOrElse(member -> member.mapDtoToEntity(memberInfoDto),
               () -> { throw new RuntimeException("회원이 존재하지 않습니다."); });
    }

    /**
     * 20명의 랜덤 이성 회원을 추천
     */
    public List<MemberCardDto> getRandomMemberList(String email) throws Exception {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);

        if (memberOptional.isEmpty()) {
            throw new Exception("회원이 존재하지 않습니다.");
        }
        Member member = memberOptional.get();
        PageRequest pageRequest = PageRequest.of(0, 20);
        return memberRepository.findRandomMember(member.getId(), member.getGender(), pageRequest);
    }

    /**
     * 나와 잘 맞는 mbti를 가진 5명의 이성 회원을 추천
     */
    public List<MemberMbtiDto> getGoodMbtiList(String email, List<MemberCardDto> randomMemberList) throws Exception {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        if (memberOptional.isEmpty()) {
            throw new Exception("회원이 존재하지 않습니다.");
        }

        Member member = memberOptional.get();
        List<String> partnerMbtiList = Mbti.getGoodPartner(member.getMbti());

        List<Long> randomMemberIdList = randomMemberList.stream().map(MemberCardDto::getId).collect(Collectors.toList());

        PageRequest pageRequest = PageRequest.of(0, 5);
        return memberRepository.findRandomMemberbyMbtiList(partnerMbtiList, randomMemberIdList, member.getId(), pageRequest);
    }

    public MemberInfoDto getMemberProfile(String email) throws Exception {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        if (memberOptional.isEmpty()) {
            throw new Exception("회원이 존재하지 않습니다.");
        }

        Member member = memberOptional.get();

        MemberInfoDto memberInfoDto = new MemberInfoDto();
        memberInfoDto.mapEntityToDto(member);
        return memberInfoDto;
    }

    @Transactional
    public void updateMemberProfile(String email, MemberInfoDto memberInfoDto) {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);

        memberOptional.ifPresentOrElse(member -> member.mapDtoToEntity(memberInfoDto),
                () -> { throw new RuntimeException("회원이 존재하지 않습니다."); });
    }
}
