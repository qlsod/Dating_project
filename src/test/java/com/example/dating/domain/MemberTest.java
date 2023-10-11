package com.example.dating.domain;

import com.example.dating.dto.MemberCardDto;
import com.example.dating.dto.MemberDto;
import com.example.dating.repository.MemberRepository;
import com.example.dating.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MemberTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 회원정보저장() {
        MemberDto memberDto = new MemberDto("user", 24, "남자", "서울시 중랑구", "이미지 경로", 180, "ISFJ", "다정함", "코딩", "ESTJ", "친절함");

        memberService.save(memberDto);
        long count = memberRepository.findAll().size();

        assertThat(count).isEqualTo(1);
    }

    @Test
    void 랜덤회원조회() {
        for (int i =0; i<50; i++) {
            memberService.save(new MemberDto("user" + i, i, "남자", "서울시 중랑구", "이미지 경로", 180, "ISFJ", "다정함", "코딩", "ESTJ", "친절함"));
        }

        List<MemberCardDto> randomMemberList = memberService.getRandomMemberList();

        assertThat(randomMemberList.size()).isEqualTo(20);
    }

}