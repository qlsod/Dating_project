package com.example.dating.controller;

import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;


    @PostMapping("/s3-upload")
    public ResponseEntity<Map<String, Object>> postImageUpload(@RequestPart(value = "file") List<MultipartFile> multipartFiles, @AuthenticationPrincipal PrincipalDetails principalDetails,
                                                               @RequestParam String type) {

        HashMap<String, Object> response = new HashMap<>();

        // 파일 존재 여부 확인
        imageService.validateFileExists(multipartFiles);

        // 토큰에 저장된 유저 ID 꺼내는 로직
        String email = principalDetails.getUsername();

        // S3 upload
        List<String> imageList = imageService.uploadS3(multipartFiles, email, type);

        response.put("imageList", imageList);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
