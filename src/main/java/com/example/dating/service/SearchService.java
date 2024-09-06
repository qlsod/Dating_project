package com.example.dating.service;

import com.example.dating.domain.Member;
import com.example.dating.domain.ProfileImage;
import com.example.dating.domain.Search;
import com.example.dating.dto.member.MemberCommonDto;
import com.example.dating.dto.search.SearchDto;
import com.example.dating.dto.search.SearchRes;
import com.example.dating.repository.MemberRepository;
import com.example.dating.repository.ProfileImagesRepository;
import com.example.dating.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {


    private final MemberRepository memberRepository;
    private final SearchRepository searchRepository;
    private final ProfileImagesRepository profileImagesRepository;


    public void post(String email, SearchDto searchDto) {

        try {

            Member member = memberRepository.findIdByEmail(email);
            Search search = new Search(member);
            search.mapDtoToEntity(searchDto);
            searchRepository.save(search);

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    public List<SearchRes> getList() {
        List<Search> searchList = searchRepository.findAll();


        // 모든 회원 ID를 조회합니다.
        List<Long> memberIds = searchList.stream()
                .map(search -> search.getMember().getId())
                .collect(Collectors.toList());

        // 2단계: 프로필 이미지 목록 조회
        List<ProfileImage> profileImages = profileImagesRepository.findProfileImagesByMemberIds(memberIds);

        // 이미지 목록을 회원과 매핑
        Map<Long, List<String>> imagesByMemberId = profileImages.stream()
                .collect(Collectors.groupingBy(
                        pi -> pi.getMember().getId(),
                        Collectors.mapping(ProfileImage::getImage, Collectors.toList())
                ));


        // Search 리스트를 SearchRes DTO 리스트로 변환합니다.

        return searchList.stream()
                .map(search -> {
                    // Member 객체를 MemberCommonDto로 변환합니다.
                    MemberCommonDto memberCommonDto = new MemberCommonDto(
                            search.getMember(),
                            imagesByMemberId.getOrDefault(search.getMember().getId(), Collections.emptyList())
                    );

                    // Search 객체를 SearchRes DTO로 변환합니다.
                    return new SearchRes(
                            search.getTitle(),
                            search.getId(),
                            memberCommonDto
                    );
                })
                .collect(Collectors.toList());
    }


}
