package com.example.dating.service;

import com.example.dating.domain.GwatingRoom;
import com.example.dating.domain.Member;
import com.example.dating.domain.RoomMember;
import com.example.dating.dto.gwating.GwatingCardDto;
import com.example.dating.dto.gwating.GwatingDetailDto;
import com.example.dating.dto.gwating.GwatingRoomDto;
import com.example.dating.dto.gwating.GwatingUpdateDto;
import com.example.dating.dto.member.MemberCardDto;
import com.example.dating.dto.member.MemberGenderDto;
import com.example.dating.dto.member.MemberInviteDto;
import com.example.dating.repository.GwatingRepository;
import com.example.dating.repository.MemberRepository;
import com.example.dating.repository.RoomMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GwatingService {

    private final GwatingRepository gwatingRepository;
    private final MemberRepository memberRepository;
    private final RoomMemberRepository roomMemberRepository;

    public List<GwatingCardDto> findRoomList(String roomCategory, String email) {
        return gwatingRepository.findAllByRoomCategory(roomCategory, email);
    }

    @Transactional
    public void create(GwatingRoomDto gwatingRoomDto, String email) {
        List<Long> inviteIdList = gwatingRoomDto.getInviteIdList();
        List<Member> inviteMemberList = memberRepository.findInviteMember(inviteIdList);

        Member findMember = memberRepository.findByEmail(email).get();

        GwatingRoom gwatingRoom = new GwatingRoom();
        gwatingRoom.createGwatingRoom(gwatingRoomDto);
        if (findMember.getGender().equals("남자")) {
            gwatingRoom.joinMale();
        } else {
            gwatingRoom.joinFemale();
        }

        inviteMemberList.stream().map(Member::getGender).forEach((g) -> {
            if (g.equals("남자")) {
                gwatingRoom.joinMale();
            } else {
                gwatingRoom.joinFemale();
            }
        });

        gwatingRepository.save(gwatingRoom);

        RoomMember roomMember = new RoomMember(gwatingRoom, findMember);
        roomMemberRepository.save(roomMember);

        for (Member inviteMember : inviteMemberList) {
            roomMember = new RoomMember(gwatingRoom, inviteMember);
            roomMemberRepository.save(roomMember);
        }
    }

    public GwatingDetailDto findRoom(Long gwatingRoomId) {
        GwatingDetailDto findGwatingRoom = gwatingRepository.findOneByIdToGwatingDetailDto(gwatingRoomId);
        findGwatingRoom.setMemberInviteDtoList(roomMemberRepository.findRoomMember());
        return findGwatingRoom;
    }

    @Transactional
    public void join(Long id, String email) throws Exception {
        Member findMember = memberRepository.findByEmail(email).get();
        GwatingRoom findRoom = gwatingRepository.findOneById(id);

        List<Long> joinMemberList = gwatingRepository.findJoinMemberList(findRoom);
        if (!joinMemberList.contains(findMember.getId())) {
            if (findMember.getGender().equals("남자")) {
                if (findRoom.getJoinMaleCount() >= findRoom.getMaleCount()) {
                    throw new Exception("남자 정원 수가 초과되어 참가가 불가능합니다.");
                }
                findRoom.joinMale();
            } else {
                if (findRoom.getJoinFemaleCount() >= findRoom.getFemaleCount()) {
                    throw new Exception("여자 정원 수가 초과되어 참가가 불가능합니다.");
                }
                findRoom.joinFemale();
            }
            RoomMember roomMember = new RoomMember(findRoom, findMember);
            roomMemberRepository.save(roomMember);
        } else {
            throw new Exception("이미 방에 참가했습니다.");
        }
    }

    public List<GwatingCardDto> searchByLocation(String location, String email) {
        return gwatingRepository.findAllByLocation('%' + location + '%', email);
    }

    @Transactional
    public void update(GwatingUpdateDto gwatingUpdateDto, Long id) {
        Optional<GwatingRoom> gwatingRoomOptional = gwatingRepository.findById(id);
        if (gwatingRoomOptional.isPresent()) {
            GwatingRoom gwatingRoom = gwatingRoomOptional.get();
            gwatingRoom.updateGwatingRoom(gwatingUpdateDto);
        }
    }
}
