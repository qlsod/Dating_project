package com.example.dating.repository;

import com.example.dating.domain.ProfileImage;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfileImagesRepository extends JpaRepository<ProfileImage, Long> {

    List<ProfileImage> findAllByMemberId(Long memberId);

    @Query("SELECT pi FROM ProfileImage pi WHERE pi.member.id IN :memberIds")
    List<ProfileImage> findProfileImagesByMemberIds(@Param("memberIds") List<Long> memberIds);

}
