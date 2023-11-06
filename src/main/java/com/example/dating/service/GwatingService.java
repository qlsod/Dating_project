package com.example.dating.service;

import com.example.dating.domain.GwatingRoom;
import com.example.dating.domain.Member;
import com.example.dating.dto.gwating.GwatingCardDto;
import com.example.dating.dto.gwating.GwatingRoomDto;
import com.example.dating.dto.member.MemberGenderDto;
import com.example.dating.repository.GwatingRepository;
import com.example.dating.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GwatingService {

    private final GwatingRepository gwatingRepository;
    private final MemberRepository memberRepository;

    public List<GwatingCardDto> findRoomList(String roomCategory) {
        return gwatingRepository.findAllByRoomCategory(roomCategory);
    }

    public String create(GwatingRoomDto gwatingRoomDto, String email) {
        Integer count = gwatingRoomDto.getAllCount();
        if (count == 0) {
            return "인원수는 최소 1명 이상입니다.";
        }

        if (count % 2 != 0) {
            return "인원수는 짝수로 입력해주세요.";
        }

        Member findMember = memberRepository.findByEmail(email).get();

        GwatingRoom gwatingRoom = new GwatingRoom();
        gwatingRoom.createGwatingRoom(gwatingRoomDto, findMember);
        gwatingRepository.save(gwatingRoom);

        return "과팅방 생성 완료";
    }

    @Transactional
    public void join(Long id, String email) {
        Member findMember = memberRepository.findByEmail(email).get();
        GwatingRoom findRoomCard = gwatingRepository.findOneById(id);

        if (findMember.getGender().equals("남자")) {
            findRoomCard.joinMale();
        } else {
            findRoomCard.joinFemale();
        }
    }
}
