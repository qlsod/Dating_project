package com.example.dating.controller;

import com.example.dating.dto.block.BlockListDto;
import com.example.dating.dto.email.EmailDto;
import com.example.dating.dto.member.MemberDeviceTokenDto;
import com.example.dating.service.EmailService;
import com.example.dating.redis.service.RedisService;
import com.example.dating.dto.member.MemberJoinDto;
import com.example.dating.dto.member.MemberInfoDto;
import com.example.dating.repository.MemberRepository;
import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.security.jwt.TokenInfo;
import com.example.dating.service.MemberService;
import com.example.dating.service.ImageService;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
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

    @PostMapping("/join")
    public ResponseEntity<Map<String, String>> join(@Validated @RequestBody MemberJoinDto memberJoinDto, BindingResult bindingResult) {
        HashMap<String, String> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            response.put("errorMessage", bindingResult.getFieldError().getDefaultMessage());
            return ResponseEntity.badRequest().body(response);
        }

        try {
            Long memberId = memberService.join(memberJoinDto);
            response.put("memberId", String.valueOf(memberId));
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 사용자 device token 저장
    @PostMapping("/deviceToken")
    public ResponseEntity<Void> saveDeviceToken(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                               @RequestParam() String deviceToken) {
        HashMap<String, String> response = new HashMap<>();

        try {
            String email = principalDetails.getUsername();
            // 키, 벨류 형식으로 redis 저장
            redisService.setDeviceToken(deviceToken, email);
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/deviceToken")
    public ResponseEntity<Void> deleteDeviceToken(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        HashMap<String, String> response = new HashMap<>();

        try {
            String email = principalDetails.getUsername();
            // email 키 값을 이용하여 redis 데이터 삭제
            redisService.delDeviceToken(email);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    

    // 프로필 생성
    @PostMapping("/profile/save")
    public ResponseEntity<MemberInfoDto> saveMemberProfile(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                           @RequestBody @Validated MemberInfoDto memberInfoDto) {

        try {
            String email = principalDetails.getUsername();

            /** 해당 이미지들을 저장하는 메소드
            **/
            memberService.saveProfileImages(email, memberInfoDto);

            memberService.save(email, memberInfoDto);

            // requestDto 내용 반환
            return ResponseEntity.status(HttpStatus.CREATED).body(memberInfoDto);
        } catch (RuntimeException e) {
            throw new RuntimeException("저장실패");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Validated @RequestBody MemberJoinDto memberJoinDto, BindingResult bindingResult) {
        HashMap<String, String> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            response.put("errorMessage", bindingResult.getFieldError().getDefaultMessage());
            return ResponseEntity.badRequest().body(response);
        }

        // 로그인 시도
        try {
            TokenInfo jwt = memberService.login(memberJoinDto);
            redisService.setValues(jwt.getRefreshToken(), memberJoinDto.getEmail());
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("RefreshToken") String refreshToken) {
        String refreshTokenValue = redisService.getValues(refreshToken.substring(7));
        if (refreshTokenValue != null) {
            redisService.delValues(refreshToken);
            return ResponseEntity.ok("로그아웃");
        } else {
            return ResponseEntity.badRequest().body("오류 발생");
        }
    }

    @PostMapping("/mail/confirm")
    public ResponseEntity<Map<String, String>> mailConfirm(@RequestParam String email) {
        HashMap<String, String> response = new HashMap<>();
//        String email = emailDto.getEmail();
        try {
            // 해당 이메일로 된 계정이 존재하지 않으면
//            if (memberRepository.findByEmail(email).isEmpty()) {
//                response.put("errorMessage", "일치하는 계정이 없습니다.");
//                return ResponseEntity.badRequest().body(response);
//            }
            // 존재하면 인증 번호를 메일로 전송
            String code = emailService.sendEmail(email);
            response.put("code", code);
            return ResponseEntity.ok(response);
        } catch (MessagingException | UnsupportedEncodingException e) {
            response.put("errorMessage", "메일 전송 실패");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/password/update")
    public ResponseEntity<Map<String, String>> updatePassword(@RequestBody EmailDto emailDto) {
        HashMap<String, String> response = new HashMap<>();
        try {
            memberService.updatePassword(emailDto);
            response.put("successMessage", "패스워드 변경 성공");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("errorMessage", "패스워드 변경 실패");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> getMemberProfile(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        try {
            MemberInfoDto memberProfile = memberService.getMemberProfile(principalDetails.getUsername());
            return ResponseEntity.ok(memberProfile);
        } catch (Exception e) {
            HashMap<String, String> response = new HashMap<>();
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<Object> getOtherMemberProfile(@PathVariable Long id) {
        try {
            MemberInfoDto otherMemberProfile = memberService.getMemberProfile(id);
            return ResponseEntity.ok(otherMemberProfile);
        } catch (Exception e) {
            HashMap<String, String> response = new HashMap<>();
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 휴먼유저는 추천에만 안 뜨게.. 설정한 뒤에 로그인하면 자동으로 휴먼계정 해제
    @PostMapping("/humanUser")
    public ResponseEntity<Map<String, String>> humanMember(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        HashMap<String, String> response = new HashMap<>();

        try {
            String email = principalDetails.getUsername();
            memberService.addHumanMember(email);

            response.put("successMessage", "휴먼계정 전환 성공");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 차단은 했고 이제 차단당한 사람과 차단한 사람이 서로 보이지 않도록 해야한다..!!
    @PostMapping("/block/{id}")
    public ResponseEntity<Map<String, String>> blockMember(@PathVariable Long id,
                                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        HashMap<String, String> response = new HashMap<>();
        try {
            String username = principalDetails.getUsername();
            memberService.block(id, username);
            response.put("successMessage", "차단되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/block")
    public ResponseEntity<Map<String, Object>> blockMemberList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        HashMap<String, Object> response = new HashMap<>();

        try {
            String email = principalDetails.getUsername();
            List<BlockListDto> blockMemberList = memberService.getBlockMemberList(email);
            response.put("blockMemberList",  blockMemberList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 차단 해제
    @PostMapping("nonblock/{id}")
    public ResponseEntity<Map<String, String>> cancelBlockMember(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                                 @PathVariable Long id) {
        HashMap<String, String> response = new HashMap<>();

        try {
            String email = principalDetails.getUsername();
            memberService.deleteBlockMember(email, id);
            response.put("successMessage", "차단이 해제됨");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/profile/update")
    public ResponseEntity<Map<String, String>> updateMemberProfile(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                                   @Validated @RequestBody MemberInfoDto memberInfoDto,
                                                                   BindingResult bindingResult) {
        HashMap<String, String> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            response.put("errorMessage", bindingResult.getFieldError().getDefaultMessage());
            return ResponseEntity.badRequest().body(response);
        }

        try {
            memberService.updateMemberProfile(principalDetails.getUsername(), memberInfoDto);
            response.put("successMessage", "프로필 수정 성공");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteMember(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        HashMap<String, String> response = new HashMap<>();

        try {
            String email = principalDetails.getUsername();
            memberService.deleteMember(email);

            response.put("successMessage", "회원 삭제 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
