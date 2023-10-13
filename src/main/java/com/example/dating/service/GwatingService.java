package com.example.dating.service;

import com.example.dating.domain.GwatingRoom;
import com.example.dating.domain.Member;
import com.example.dating.dto.gwating.GwatingCardDto;
import com.example.dating.dto.gwating.GwatingRoomDto;
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
        List<GwatingCardDto> roomList = gwatingRepository.findAllByRoomCategory(roomCategory);
        return roomList;
    }

    public String create(GwatingRoomDto gwatingRoomDto, String email) {
        Integer count = gwatingRoomDto.getAllCount();
        if (count == 0) {
            return "인원수는 최소 1명 이상입니다.";
        }

        if (count % 2 != 0) {
            return "인원수는 짝수로 입력해주세요.";
        }

        Member myMember = memberRepository.findMyMember(email);

        GwatingRoom gwatingRoom = new GwatingRoom();
        gwatingRoom.createGwatingRoom(gwatingRoomDto, myMember);

        gwatingRepository.save(gwatingRoom);

        return "과팅방 생성 완료";
    }

    @Transactional
    public void join(Long id, String email) {
        String myGender = memberRepository.findMyGender(email);
        GwatingRoom findRoomCard = gwatingRepository.findOneById(id);

        if (myGender.equals("남자")) {
            findRoomCard.joinMale();
        } else {
            findRoomCard.joinFemale();
        }
    }
}
