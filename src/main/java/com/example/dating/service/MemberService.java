package com.example.dating.service;

import com.example.dating.domain.Block;
import com.example.dating.domain.HumanMember;
import com.example.dating.domain.Member;
import com.example.dating.dto.block.BlockListDto;
import com.example.dating.dto.email.EmailDto;
import com.example.dating.dto.member.*;
import com.example.dating.mbti.Mbti;
import com.example.dating.repository.BlockRepository;
import com.example.dating.repository.HumanMemberRepository;
import com.example.dating.repository.MemberRepository;
import com.example.dating.security.jwt.TokenInfo;
import com.example.dating.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    private final BlockRepository blockRepository;
    private final HumanMemberRepository humanMemberRepository;

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

        // 휴먼계정이면 휴먼계정 해제
        Optional<HumanMember> humanMemberOptional = humanMemberRepository.findByEmail(memberJoinDto.getEmail());
        if (humanMemberOptional.isPresent()) {
            HumanMember humanMember = humanMemberOptional.get();
            humanMemberRepository.delete(humanMember);
        }

        // jwt 발급 (임시 권한 User 부여)
        Authentication token = new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword(), List.of(new SimpleGrantedAuthority("User")));

        return tokenProvider.generateToken(token);
    }

    public List<MemberInviteDto> getAllMember(String email) {
        return memberRepository.findAllNotContainMe(email);
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
    public List<MemberCardDto> getRandomMemberList(String email) {
        Member findMember = memberRepository.findByEmail(email).get();

        PageRequest pageRequest = PageRequest.of(0, 20);
        return memberRepository.findRandomMember(findMember.getId(), findMember.getGender(), pageRequest);
    }

    /**
     * 나와 잘 맞는 mbti를 가진 5명의 이성 회원을 추천
     */
    public List<MemberMbtiDto> getGoodMbtiList(String email, List<MemberCardDto> randomMemberList) {
        Member findMember = memberRepository.findByEmail(email).get();

        List<String> partnerMbtiList = Mbti.getGoodPartner(findMember.getMbti());

        List<Long> randomMemberIdList = randomMemberList.stream().map(MemberCardDto::getId).collect(Collectors.toList());

        PageRequest pageRequest = PageRequest.of(0, 5);
        return memberRepository.findRandomMemberbyMbtiList(partnerMbtiList, randomMemberIdList, findMember.getId(), pageRequest);
    }

    public List<MemberCardDto> getSendHeartList(String email) {
        return memberRepository.findSendHeartList(email);
    }

    public List<MemberCardDto> getReceiverHeartList(String email) {
        return memberRepository.findReceiverHeartList(email);
    }

    public MemberInfoDto getMemberProfile(String email) {
        Member findMember = memberRepository.findByEmail(email).get();

        MemberInfoDto memberInfoDto = new MemberInfoDto();
        memberInfoDto.mapEntityToDto(findMember);
        return memberInfoDto;
    }

    public MemberInfoDto getMemberProfile(Long id) {
        Member findMember = memberRepository.findById(id).get();

        MemberInfoDto memberInfoDto = new MemberInfoDto();
        memberInfoDto.mapEntityToDto(findMember);
        return memberInfoDto;
    }

    @Transactional
    public void block(Long id, String email) throws Exception {
        Member findBlockItMember = memberRepository.findByEmail(email).get();
        Optional<Member> blockMemberOptional = memberRepository.findById(id);
        if (blockMemberOptional.isEmpty()) {
            throw new Exception("차단하려는 사용자가 존재하지 않습니다.");
        }
        Member blockMember = blockMemberOptional.get();

        Block block = new Block(findBlockItMember, blockMember);
        blockRepository.save(block);
    }

    @Transactional
    public void updateMemberProfile(String email, MemberInfoDto memberInfoDto) {
        Member findMember = memberRepository.findByEmail(email).get();

        findMember.mapDtoToEntity(memberInfoDto);
    }

    @Transactional
    public void updatePassword(EmailDto emailDto) {
        Member member = memberRepository.findByEmail(emailDto.getEmail()).get();
        String encodePassword = passwordEncoder.encode(emailDto.getPassword());
        member.updatePassword(encodePassword);
    }

    @Transactional
    public void deleteMember(String email) {
        memberRepository.deleteByEmail(email);
    }

    @Transactional
    public void addHumanMember(String email) {
        Member member = memberRepository.findByEmail(email).get();

        HumanMember humanMember = new HumanMember(member);
        humanMemberRepository.save(humanMember);
    }

    public List<BlockListDto> getBlockMemberList(String email) {
        return blockRepository.findByEmail(email);
    }

    @Transactional
    public void deleteBlockMember(String email, Long id) {
        Long myId = memberRepository.findByEmail(email).get().getId();
        blockRepository.deleteBlockMemberById(myId, id);
    }
}
