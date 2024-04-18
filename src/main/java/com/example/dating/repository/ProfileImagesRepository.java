package com.example.dating.repository;

import com.example.dating.domain.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileImagesRepository extends JpaRepository<ProfileImage, Long> {

    List<ProfileImage> findAllByMemberId(Long memberId);

}
