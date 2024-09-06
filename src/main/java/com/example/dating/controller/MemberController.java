package com.example.dating.controller;

import com.example.dating.domain.Member;
import com.example.dating.dto.block.BlockListDto;
import com.example.dating.dto.email.EmailDto;
import com.example.dating.dto.member.*;
import com.example.dating.dto.response.member.MemberBlockListRes;
import com.example.dating.dto.response.member.MemberJoinRes;
import com.example.dating.dto.response.member.MemberMailRes;
import com.example.dating.service.EmailService;
import com.example.dating.redis.service.RedisService;
import com.example.dating.repository.MemberRepository;
import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.security.jwt.TokenInfo;
import com.example.dating.service.MemberService;
import com.example.dating.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final RedisService redisService;
    private final EmailService emailService;
    private final MemberRepository memberRepository;
    private final ImageService imageService;

    @Operation(summary = "사용자 회원가입",
            description = "사용자 email 기반 회원가입을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "해당 사용자 uuid 반환"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @PostMapping("/join")
    public ResponseEntity<MemberJoinRes> join(@Validated @RequestBody MemberJoinDto memberJoinDto) {

        try {
            Long memberId = memberService.join(memberJoinDto);
            MemberJoinRes memberJoinRes = new MemberJoinRes(memberId);
            return ResponseEntity.status(HttpStatus.CREATED).body(memberJoinRes);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    // 사용자 device token 저장
    @Operation(summary = "사용자 deviceToken 저장",
            description = "fcm 위한 deviceToken 저장")
    @SecurityRequirement(name = "accessToken")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "저장 성공"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @PostMapping("/deviceToken")
    public ResponseEntity<Void> saveDeviceToken(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                               @RequestParam() String deviceToken) {
        try {
            String email = principalDetails.getUsername();
            // 키, 벨류 형식으로 redis 저장
            redisService.setDeviceToken(deviceToken, email);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "사용자 deviceToken 삭제",
            description = "로그아웃 및 회원탈퇴 시 fcm 위한 deviceToken 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @SecurityRequirement(name = "accessToken")
    @DeleteMapping("/deviceToken")
    public ResponseEntity<Void> deleteDeviceToken(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        HashMap<String, String> response = new HashMap<>();

        try {
            String email = principalDetails.getUsername();
            // email 키 값을 이용하여 redis 데이터 삭제
            redisService.delDeviceToken(email);
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 프로필 생성
    @Operation(summary = "프로필 저장",
            description = "사용자 프로필을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "저장된 프로필 내용 반환"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @SecurityRequirement(name = "accessToken")
    @PostMapping("/profile/save")
    public ResponseEntity<MemberInfoDto> saveMemberProfile(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                           @RequestBody @Validated MemberInfoDto memberInfoDto) {
        try {
            String email = principalDetails.getUsername();

            // 이미지 저장 메소드
            memberService.saveProfileImages(email, memberInfoDto);

            // 해당 내용 전체 저장
            memberService.save(email, memberInfoDto);

            return ResponseEntity.status(HttpStatus.CREATED).body(memberInfoDto);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * 다음 작업
     */
    @Operation(summary = "프로필 수정",
            description = "사용자 프로필을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장된 프로필 내용 반환"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @SecurityRequirement(name = "accessToken")
    @PostMapping("/profile/update")
    public ResponseEntity<MemberInfoDto> updateMemberProfile(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                             @Validated @RequestBody MemberInfoDto memberInfoDto) {
        try {

            String email = principalDetails.getUsername();

            // 이미지들을 저장하는 메소드
            memberService.updateProfileImages(email, memberInfoDto);

            memberService.save(email, memberInfoDto);

            return ResponseEntity.status(HttpStatus.OK).body(memberInfoDto);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "닉네임 중복 체크",
            description = "해당 닉네임이 등록되어 있는지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "중복 되지 않은 닉네임 입니다."),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @PostMapping("/check/nick-name")
    public ResponseEntity<Void> checkNickName(@RequestBody MemberNickNameDto memberNickNameDto) {

        try {
            // 닉네임 중복 여부 조회
            memberService.checkNameExist(memberNickNameDto.getNickName());

            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "로그인",
            description = "회원정보 확인 후 JWT 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JWT 반환"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenInfo> login(@Validated @RequestBody MemberJoinDto memberJoinDto) {

        // 로그인 시도
        try {
            TokenInfo jwt = memberService.login(memberJoinDto);
            redisService.setValues(jwt.getRefreshToken(), memberJoinDto.getEmail());
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "로그아웃",
            description = "회원정보 확인 후 Redis에 저장된 RefreshToken을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("RefreshToken") String refreshToken) {

        try {
            if (refreshToken == null) {
                throw new RuntimeException("RefreshToken이 존재하지 않습니다.");
            }

            String refreshTokenValue = redisService.getValues(refreshToken.substring(7));
            if (refreshTokenValue == null) {
                throw new RuntimeException("유효하지 않은 토큰 정보입니다.");
            }
            redisService.delValues(refreshToken);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    @Operation(summary = "메일인증",
            description = "사용자 email로 랜덤난수 생성하여 보냅니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "랜덤난수 반환"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @PostMapping("/mail/confirm")
    public ResponseEntity<MemberMailRes> mailConfirm(@RequestBody @Valid MemberMailDto memberMailDto) {
        try {
            // 존재하면 인증 번호를 메일로 전송
            String email = memberMailDto.getEmail();
            String code = emailService.sendEmail(email);
            MemberMailRes mailRes = new MemberMailRes(code);
            return ResponseEntity.ok(mailRes);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "본인 프로필 조회",
            description = "사용자 본인 프로필 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 정보 반환"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @SecurityRequirement(name = "accessToken")
    @GetMapping("/profile")
    public ResponseEntity<MemberCommonDto> getMemberProfile(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        try {
            MemberCommonDto memberProfile = memberService.getMemberProfileByEmail(principalDetails.getUsername());
            return ResponseEntity.ok(memberProfile);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "타인 프로필 조회(uuid)",
            description = "uuid 이용한 타켓 사용자 프로필 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 내용 반환"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @GetMapping("/profile/another/{id}")
    public ResponseEntity<MemberCommonDto> getOtherMemberProfileByUUID(@PathVariable Long id) {
        try {
            MemberCommonDto otherMemberProfile = memberService.getMemberProfileById(id);
            return ResponseEntity.ok(otherMemberProfile);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "타인 프로필 조회(닉네임)",
            description = "닉네임 이용한 타켓 사용자 프로필 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 내용 반환"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @GetMapping("/profile/another/nick-name")
    public ResponseEntity<MemberCommonDto> getOtherMemberProfileByNickName(@RequestParam("nickName") String nickName) {
        try {
            MemberCommonDto otherMemberProfile = memberService.getMemberProfileByName(nickName);
            return ResponseEntity.ok(otherMemberProfile);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    // 휴먼유저는 추천에만 안 뜨게.. 설정한 뒤에 로그인하면 자동으로 휴먼계정 해제
    @Operation(summary = "휴먼유저 설정",
            description = "사용자 본인을 휴먼유저로 전환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전환 성공"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @SecurityRequirement(name = "accessToken")
    @PostMapping("/humanUser")
    public ResponseEntity<Void> humanMember(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        try {
            String email = principalDetails.getUsername();
            memberService.addHumanMember(email);

            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    // 차단은 했고 이제 차단당한 사람과 차단한 사람이 서로 보이지 않도록 해야한다..!!
    @Operation(summary = "다른 사용자 차단",
            description = "타켓 사용자를 차단처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "차단 성공"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @SecurityRequirement(name = "accessToken")
    @PostMapping("/block/{id}")
    public ResponseEntity<Void> blockMember(@PathVariable Long id,
                                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {

        try {
            String username = principalDetails.getUsername();
            memberService.block(id, username);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "차단 사용자 목록 조회",
            description = "사용자 본인이 차단한 사용자의 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "차단 사용자 목록 반환"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @SecurityRequirement(name = "accessToken")
    @GetMapping("/block")
    public ResponseEntity<List<MemberCommonDto>> blockMemberList(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        try {
            String email = principalDetails.getUsername();
            List<MemberCommonDto> blockMemberList = memberService.getBlockMemberList(email);
            return ResponseEntity.ok(blockMemberList);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    // 차단 해제
    @Operation(summary = "다른 사용자 차단 해제",
            description = "타켓 사용자의 차단을 해제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "차단 해제 성공"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @SecurityRequirement(name = "accessToken")
    @PostMapping("nonblock/{id}")
    public ResponseEntity<Void> cancelBlockMember(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                                 @PathVariable Long id) {
        try {
            String email = principalDetails.getUsername();
            memberService.deleteBlockMember(email, id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }



    @Operation(summary = "회원 탈퇴",
            description = "사용자 정보를 전부 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "탈퇴 성공"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @SecurityRequirement(name = "accessToken")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteMember(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        try {
            String email = principalDetails.getUsername();
            memberService.deleteMember(email);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
