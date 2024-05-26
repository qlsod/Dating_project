package com.example.dating.controller;

import com.example.dating.domain.Member;
import com.example.dating.domain.Search;
import com.example.dating.dto.search.SearchDetailRes;
import com.example.dating.dto.search.SearchDto;
import com.example.dating.dto.search.SearchRes;
import com.example.dating.repository.MemberRepository;
import com.example.dating.repository.SearchRepository;
import com.example.dating.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final MemberRepository memberRepository;
    private final SearchRepository searchRepository;

    @PostMapping("")
    public ResponseEntity<SearchDto> postSearch(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody @Valid SearchDto searchDto) {

        try {
            // 유저 email 꺼내기
            String email = principalDetails.getUsername();

            Member member = memberRepository.findIdByEmail(email);
            Search search = new Search(member);
            search.mapDtoToEntity(searchDto);

            searchRepository.save(search);

            return ResponseEntity.status(HttpStatus.CREATED).body(searchDto);
        } catch (Exception e) {
            throw new RuntimeException("search중 오류 발생", e);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<SearchRes>> getSearch() {
        List<Search> searchList = searchRepository.findAll();
        List<SearchRes> searchResList = searchList.stream()
                .map(search -> {
                    SearchRes searchRes = new SearchRes();
                    searchRes.entityToDto(search);
                    return searchRes;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(searchResList);


    }

    @GetMapping("/{id}")
    public ResponseEntity<SearchDetailRes> getSearchDetail(@PathVariable Long id) {

        Search search = searchRepository.findSearchById(id);
        SearchDetailRes searchDetailRes = new SearchDetailRes();
        searchDetailRes.entityToDto(search);
        return ResponseEntity.ok(searchDetailRes);

    }

}
