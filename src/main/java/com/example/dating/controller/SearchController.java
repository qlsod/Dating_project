package com.example.dating.controller;

import com.example.dating.domain.Search;
import com.example.dating.dto.search.*;
import com.example.dating.repository.MemberRepository;
import com.example.dating.repository.SearchRepository;
import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    private final SearchService searchService;
    @Operation(summary = "탐색창 글쓰기",
            description = "해당 사용자가 작성한 글을 저장합니다.")
    @SecurityRequirement(name = "accessToken")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "저장된 해당 글 내용 반환"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @PostMapping("")
    public ResponseEntity<SearchDto> postSearch(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                @RequestBody @Valid SearchDto searchDto) {
        try {
            // 유저 email 꺼내기
            String email = principalDetails.getUsername();
            searchService.post(email, searchDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(searchDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "탐색창 List 불러오기 pagination(20개) - 처음 시도 시",
            description = "최신 탐색창 글 20개 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "list 최신 20개 반환"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @GetMapping("list")
    public ResponseEntity<List<SearchRes>> getSearch() {

        List<SearchRes> searchResList = searchService.getListFirst();

        return ResponseEntity.ok(searchResList);

    }

    @Operation(summary =  "탐색창 List 불러오기 pagination(20개)",
            description = "불러온 List의 마지막 id를 입력하여 Paging 처리 된 List 새로 불러오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "list 최신 20개 반환"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @GetMapping("list/{id}")
    public ResponseEntity<List<SearchRes>> getSearchListPagination(@PathVariable("id") Long id) {
        List<SearchRes> searchResList = searchService.getPagingList(id);

        return ResponseEntity.ok(searchResList);
    }

//    @Operation(summary = "탐색창 세부 조회",
//            description = "타켓 탐색창 글의 id를 입력받아 세부 내용을 조회합니다.")
//    @SecurityRequirement(name = "accessToken")
//    @GetMapping("/{id}")
//    public ResponseEntity<SearchDetailRes> getSearchDetail(@PathVariable Long id) {
//
//        Search search = searchRepository.findSearchById(id);
//        SearchDetailRes searchDetailRes = new SearchDetailRes();
//        searchDetailRes.entityToDto(search);
//        return ResponseEntity.ok(searchDetailRes);
//
//    }

    @Operation(summary =  "내가 쓴 탐색창 List 불러오기",
            description = "탐색창의 id 입력하여 해당 탐색창 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteSearch(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                             @PathVariable("id") Long id) {
        String email = principalDetails.getUsername();

        searchService.deleteSearch(email, id);
        return ResponseEntity.ok().build();
    }


    @Operation(summary =  "해당 유저가 쓴 탐색창 List 불러오기",
            description = "유저의 닉네임 입력받아 해당 유저가 쓴 탐색창 List 전체 불러오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "list 최신 20개 반환"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @GetMapping("history")
    public ResponseEntity<List<SearchHistoryRes>> getSearchListHistory(@RequestBody SearchHistoryDto searchHistoryDto) {
        List<SearchHistoryRes> SearchHistoryResList = searchService.getSearchHistory(searchHistoryDto);

        return ResponseEntity.ok(SearchHistoryResList);
    }

    @Operation(summary = "탐색창 글 수정",
            description = "해당 사용자가 작성한 글을 수정합니다.")
    @SecurityRequirement(name = "accessToken")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "list 최신 20개 반환"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @PatchMapping("")
    public ResponseEntity<SearchPatchDto> updateSearch(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                @RequestBody @Valid SearchPatchDto searchPatchDto) {
        try {
            // 유저 email 꺼내기
            String email = principalDetails.getUsername();
            searchService.update(email, searchPatchDto);
            return ResponseEntity.status(HttpStatus.OK).body(searchPatchDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
