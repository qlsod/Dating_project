package com.example.dating.service;

import com.example.dating.domain.Member;
import com.example.dating.dto.setting.ChangePasswordDto;
import com.example.dating.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettingService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void changePassword(String email, ChangePasswordDto changePasswordDto) throws Exception {
        Member findMember = memberRepository.findByEmail(email).get();
        String newPassword = changePasswordDto.getNewPassword();

        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), findMember.getPassword())) {
            throw new RuntimeException("입력한 현재 비밀번호가 일치하지 않습니다.");
        }
        if (newPassword.length() <= 8) {
            throw new RuntimeException("새 비밀번호는 8자 이상입니다.");
        }
        if (changePasswordDto.getCurrentPassword().equals(newPassword)) {
            throw new RuntimeException("새 비밀번호는 현재 비밀번호와 같을 수 없습니다.");
        }
        if (!newPassword.equals(changePasswordDto.getCheckNewPassword())) {
            throw new RuntimeException("새 비밀번호가 확인 비밀번호와 일치하는지 확인해주세요.");
        }

        String encodePassword = passwordEncoder.encode(newPassword);
        findMember.updatePassword(encodePassword);
    }
}
