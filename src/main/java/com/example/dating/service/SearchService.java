package com.example.dating.service;

import com.example.dating.domain.Member;
import com.example.dating.domain.ProfileImage;
import com.example.dating.domain.Search;
import com.example.dating.dto.member.MemberCommonDto;
import com.example.dating.dto.search.*;
import com.example.dating.repository.MemberRepository;
import com.example.dating.repository.ProfileImagesRepository;
import com.example.dating.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final MemberService memberService;


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

    @Transactional
    public void update(String email, SearchPatchDto searchPatchDto) {

        isAuthor(email, searchPatchDto.getId());
        Search search = searchRepository.findSearchById(searchPatchDto.getId());
        search.updateEntity(searchPatchDto);

    }

    // 해당 유저가 작성한 글인지 확인
    public void isAuthor(String email, Long id) {
        Search search = searchRepository.checkAuthor(email, id);
        if (search == null) {
            throw new RuntimeException("해당 유저가 작성한 글이 아닙니다.");
        }
    }


    public List<SearchHistoryRes> getSearchHistory(SearchHistoryDto searchHistoryDto) {

        memberService.checkMemberExistsByNickName(searchHistoryDto.getNickName());

        return searchRepository.findSearchHistory(searchHistoryDto.getNickName());
    }

    public List<SearchRes> getPagingList(Long id) {

        PageRequest pageable = PageRequest.of(0, 20);


        List<Search> searchList = searchRepository.findPagingSearch(id, pageable);

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
                            search.getContent(),
                            search.getId(),
                            search.getCreatedAt(),
                            memberCommonDto
                    );
                })
                .collect(Collectors.toList());
    }

    public List<SearchRes> getListFirst() {

        PageRequest pageable = PageRequest.of(0, 20);

        List<Search> searchList = searchRepository.findPagingSearchFirst(pageable);

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
                            search.getContent(),
                            search.getId(),
                            search.getCreatedAt(),
                            memberCommonDto
                    );
                })
                .collect(Collectors.toList());
    }

    public void deleteSearch(String email, Long id) {
        checkSearchExist(id);
        isAuthor(email, id);
        searchRepository.deleteById(id);
    }


    // 해당 글 존재 여부 확인
    public void checkSearchExist(Long id) {
        Search search = searchRepository.findSearchById(id);
        if (search == null ) {
            throw new RuntimeException("해당 글이 존재 하지 않습니다.");
        }
    }



}
