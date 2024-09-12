package com.example.dating.service;

import com.example.dating.domain.Alert;
import com.example.dating.domain.Heart;
import com.example.dating.domain.Member;
import com.example.dating.domain.ProfileImage;
import com.example.dating.dto.heart.HeartMemberDto;
import com.example.dating.dto.member.MemberCommonDto;
import com.example.dating.exception.DuplicateDataException;
import com.example.dating.exception.EntityNotFoundException;
import com.example.dating.repository.AlertRepository;
import com.example.dating.repository.HeartRepository;
import com.example.dating.repository.MemberRepository;
import com.example.dating.repository.ProfileImagesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;
    private final AlertService alertService;
    private final ProfileImagesRepository profileImagesRepository;

    /**
     * 하트를 보낸 사람과 받은 사람을 저장
     */
    @Transactional(rollbackFor = {EntityNotFoundException.class, DuplicateDataException.class})
    public void heart(Long id, String email) {
        Member sendMember = memberRepository.findByEmail(email).get();
        Optional<Member> receiverMemberOptional = memberRepository.findById(id);

        receiverMemberOptional.ifPresent(receiverMember -> {
            if (heartRepository.countBySenderAndReceiver(sendMember, receiverMember) != 0) {
                throw new DuplicateDataException("이미 하트를 보낸 회원입니다.");
            }
        });

        Member receiverMember = receiverMemberOptional.get();

        Heart heart = new Heart(sendMember, receiverMember);
        heartRepository.save(heart);

//        alertService.saveAlert(id, "heart", "하트를 받았습니다", );

//        // 받는 사람 알림창에 뜰 내용 DB에 저장
//        Alert alert = Alert.builder()
//                .receiverMember(receiverMember)
//                .image(sendMember.getImage())
//                .name(sendMember.getNickName())
//                .message("나에게 하트를 눌렀어요! 마음에 드시나요?")
//                .sendAt(LocalDate.now().toString())
//                .isCheck(false)
//                .build();
//        alertRepository.save(alert);


    }

    /**
     * 내가 하트를 보낸 리스트에서 최근 20명 조회
     */
    public List<MemberCommonDto> sendHeartList(String email) {
        PageRequest pageable = PageRequest.of(0, 20);
        List<Member> members = heartRepository.findMemberBySender(email, pageable);

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
     * 내가 하트를 받은 리스트에서 최근 20명 조회
     */
    public List<MemberCommonDto> receiverHeartList(String email) {
        // 1단계: 회원 목록 조회
        PageRequest pageable = PageRequest.of(0, 20);
        List<Member> members = heartRepository.findByReceiver(email, pageable);

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


    public List<MemberCommonDto> pagingReceiverHeartList(String email, Long id) {
        // 1단계: 회원 목록 조회

        // 일단 10명 TEST
        PageRequest pageable = PageRequest.of(0, 20);
        List<Member> members = heartRepository.findPagingMemberByReceiver(email, id, pageable);

        log.info(members.toString());

        // 2단계: 프로필 이미지 목록 조회 및 매핑
        Map<Long, List<String>> imagesByMemberId = getProfileImagesByMemberIds(members);

        // 3단계: MemberCommonDto로 변환하여 반환
        return mapMembersToDto(members, imagesByMemberId);
    }

    public List<MemberCommonDto> pagingSenderHeartList(String email, Long id) {
        // 1단계: 회원 목록 조회

        PageRequest pageable = PageRequest.of(0, 20);
        List<Member> members = heartRepository.findPagingMemberBySender(email, id, pageable);

        // 2단계: 프로필 이미지 목록 조회 및 매핑
        Map<Long, List<String>> imagesByMemberId = getProfileImagesByMemberIds(members);

        // 3단계: MemberCommonDto로 변환하여 반환
        return mapMembersToDto(members, imagesByMemberId);
    }



    // 프로필 이미지 목록을 조회하고 회원 ID와 매핑
    public Map<Long, List<String>> getProfileImagesByMemberIds(List<Member> members) {
        List<Long> memberIds = members.stream()
                .map(Member::getId)
                .collect(Collectors.toList());

        List<ProfileImage> profileImages = profileImagesRepository.findProfileImagesByMemberIds(memberIds);

        return profileImages.stream()
                .collect(Collectors.groupingBy(
                        pi -> pi.getMember().getId(),
                        Collectors.mapping(ProfileImage::getImage, Collectors.toList())
                ));
    }

    // 회원 목록과 이미지 매핑 데이터를 사용하여 MemberCommonDto로 변환
    public List<MemberCommonDto> mapMembersToDto(List<Member> members, Map<Long, List<String>> imagesByMemberId) {
        return members.stream()
                .map(member -> {
                    List<String> images = imagesByMemberId.getOrDefault(member.getId(), Collections.emptyList());
                    return new MemberCommonDto(member, images);
                })
                .collect(Collectors.toList());
    }


}
