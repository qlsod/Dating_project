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
import java.util.Optional;
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
    public String save(String email, MemberInfoDto memberInfoDto) {
        Optional<Account> accoutOptional = accountRepository.findByEmail(email);

        if (accoutOptional.isEmpty()) {
            return "존재하지 않는 이메일입니다.";
        }
        Account account = accoutOptional.get();

        Member member = new Member();
        member.mapDtoToEntity(memberInfoDto);
        member.setAccount(account);

        memberRepository.save(member);
        return "회원정보 저장 성공";
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

    public MemberInfoDto getMemberProfile(String email) {
        Member member = memberRepository.findMyMember(email);

        return MemberInfoDto.builder()
                .name(member.getName())
                .comment(member.getComment())
                .gender(member.getGender())
                .residence(member.getResidence())
                .age(member.getAge())
                .height(member.getHeight())
                .image(member.getImage())
                .personalInfo(member.getPersonalInfo())
                .mbti(member.getMbti())
                .interest(member.getInterest())
                .personality(member.getPersonality())
                .likePersonality(member.getLikePersonality())
                .build();
    }

    @Transactional
    public String updateMemberProfile(String email, MemberInfoDto memberInfoDto) {
        try {
            Member member = memberRepository.findMyMember(email);
            member.mapDtoToEntity(memberInfoDto);
        } catch (Exception e) {
            return "회원정보 수정 실패";
        }
        return "회원정보 수정 성공";
    }
}
