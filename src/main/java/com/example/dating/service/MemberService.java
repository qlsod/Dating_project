package com.example.dating.service;

import com.example.dating.domain.Block;
import com.example.dating.domain.HumanMember;
import com.example.dating.domain.Member;
import com.example.dating.domain.ProfileImage;
import com.example.dating.dto.block.BlockListDto;
import com.example.dating.dto.email.EmailDto;
import com.example.dating.dto.member.*;
import com.example.dating.repository.*;
import com.example.dating.security.jwt.TokenInfo;
import com.example.dating.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final BlockRepository blockRepository;
    private final HumanMemberRepository humanMemberRepository;
    private final ProfileImagesRepository profileImagesRepository;

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
    public void save(String email, MemberInfoDto memberInfoDto) {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        memberOptional.ifPresentOrElse(member ->
                member.mapDtoToEntity(memberInfoDto), ()
                -> { throw new RuntimeException("회원이 존재하지 않습니다."); });
    }


    /**
     * 입력한 프로필 사진들 저장
     */
    @Transactional
    public void saveProfileImages(String email, MemberInfoDto memberInfoDto) {
        Member member = memberRepository.findByEmail(email).get();

        // 회원 조회
        checkMemberExists(member);

        // 닉네임 중복 여부 조회
        checkNameExist(memberInfoDto.getNickName());

        // 이미지 URL 리스트 가져오기
        List<String> imageUrls = memberInfoDto.getImages();

        // 이미지 URL들을 ProfileImage 엔티티로 생성하여 저장
        List<ProfileImage> profileImages = imageUrls.stream()
                .map(imageUrl -> new ProfileImage(member, imageUrl))
                .collect(toList());

        profileImagesRepository.saveAll(profileImages);
    }

    /**
     * 수정한 프로필 사진들 저장
     */
    @Transactional
    public void updateProfileImages(String email, MemberInfoDto memberInfoDto) {
        Member member = memberRepository.findByEmail(email).get();

        // 회원 조회
        checkMemberExists(member);

        // 닉네임 중복 여부 조회
        checkNameExist(memberInfoDto.getNickName());

        // 해당 회원의 저장된 이미지 URL 리스트 가져오기
        List<ProfileImage> existingProfileImages = profileImagesRepository.findAllByMemberId(member.getId());

        // 기존 이미지 존재할 시 삭제
        if (!existingProfileImages.isEmpty()) {
            profileImagesRepository.deleteAll(existingProfileImages);
        }

        // 이미지 URL 리스트 가져오기
        List<String> imageUrls = memberInfoDto.getImages();

        // 이미지 URL들을 ProfileImage 엔티티로 생성하여 저장
        List<ProfileImage> profileImages = imageUrls.stream()
                .map(imageUrl -> new ProfileImage(member, imageUrl))
                .collect(toList());

        profileImagesRepository.saveAll(profileImages);
    }

    // 닉네임 존재 여부 확인
    public void checkNameExist(String nickName) {
        Member member = memberRepository.findByNickName(nickName);
        if(member != null) {
            throw new RuntimeException("해당 닉네임이 존재합니다.");
        }
    }



    /**
     * 20명의 랜덤 이성 회원을 추천
     */
    public List<MemberCommonDto> getRandomMemberList(String email) {
        Member findMember = memberRepository.findByEmail(email).get();

        log.info(String.valueOf(findMember));

        PageRequest pageRequest = PageRequest.of(0, 20);
//        return memberRepository.findRandomMember(findMember.getId(), findMember.getGender(), pageRequest);
        List<Member> members = memberRepository.findRandomRecommendMemberList(findMember.getGender(), pageRequest);

        List<Long> memberIds = members.stream()
                .map(Member::getId)
                .collect(Collectors.toList());

        // 2단계: 프로필 이미지 목록 조회
        List<ProfileImage> profileImages = profileImagesRepository.findProfileImagesByMemberIds(memberIds);

        // 이미지 목록을 회원과 매핑
        Map<Long, List<String>> imagesByMemberId = profileImages.stream()
                .collect(Collectors.groupingBy(
                        pi -> pi.getMember().getId(),
                        Collectors.mapping(ProfileImage::getImage, Collectors.toList())
                ));

        return members.stream().map(member -> {
            List<String> images = imagesByMemberId.getOrDefault(member.getId(), Collections.emptyList());
            return new MemberCommonDto(member, images);
        }).collect(Collectors.toList());

    }

    /**
     * 나와 잘 맞는 mbti를 가진 5명의 이성 회원을 추천
     */
//    public List<MemberMbtiDto> getGoodMbtiList(String email, List<MemberCardDto> randomMemberList) {
//        Member findMember = memberRepository.findByEmail(email).get();
//
////        List<String> partnerMbtiList = Mbti.getGoodPartner(findMember.getMbti());
//
//        List<Long> randomMemberIdList = randomMemberList.stream().map(MemberCardDto::getId).collect(Collectors.toList());
//
//        PageRequest pageRequest = PageRequest.of(0, 5);
//        return memberRepository.findRandomMemberbyMbtiList(partnerMbtiList, randomMemberIdList, findMember.getId(), pageRequest);
//    }

    public List<MemberCardDto> getSendHeartList(String email) {
        return memberRepository.findSendHeartList(email);
    }

    public List<MemberCardDto> getReceiverHeartList(String email) {
        return memberRepository.findReceiverHeartList(email);
    }

    // 유저 가입여부 판단 메소드
    public void checkMemberExists(Member member) {
        if (member == null) {
            throw new NullPointerException("해당 유저는 가입되어 있지 않습니다.");
        }
    }

    public MemberCommonDto getMemberProfileByName(String nickName) {
        Member findMember = memberRepository.findByNickName(nickName);
        checkMemberExists(findMember);
        List<ProfileImage> profileImages = profileImagesRepository.findAllByMemberId(findMember.getId());

        // 이미지 URL 리스트를 생성하여 DTO에 추가
        List<String> imageUrls = new ArrayList<>();
        for (ProfileImage profileImage : profileImages) {
            imageUrls.add(profileImage.getImage());
        }

        return new MemberCommonDto(findMember, imageUrls);
    }

    public MemberCommonDto getMemberProfileById(Long id) {
        Member findMember = memberRepository.findById(id).get();
        checkMemberExists(findMember);
        List<ProfileImage> profileImages = profileImagesRepository.findAllByMemberId(findMember.getId());

        // 이미지 URL 리스트를 생성하여 DTO에 추가
        List<String> imageUrls = new ArrayList<>();
        for (ProfileImage profileImage : profileImages) {
            imageUrls.add(profileImage.getImage());
        }

        return new MemberCommonDto(findMember, imageUrls);
    }

    public MemberCommonDto getMemberProfileByEmail(String email) {
        Member findMember = memberRepository.findIdByEmail(email);
        checkMemberExists(findMember);
        List<ProfileImage> profileImages = profileImagesRepository.findAllByMemberId(findMember.getId());

        // 이미지 URL 리스트를 생성하여 DTO에 추가
        List<String> imageUrls = new ArrayList<>();
        for (ProfileImage profileImage : profileImages) {
            imageUrls.add(profileImage.getImage());
        }

        return new MemberCommonDto(findMember, imageUrls);
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
