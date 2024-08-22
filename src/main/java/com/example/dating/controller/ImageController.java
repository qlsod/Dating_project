package com.example.dating.controller;

import com.example.dating.dto.response.image.ImageRes;
import com.example.dating.security.auth.PrincipalDetails;
import com.example.dating.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "S3 사진 업로드",
            description = "MultipartFiles 받아 S3 업로드하여 해당 파일 String으로 반환")
    @SecurityRequirement(name = "accessToken")
    @PostMapping("/s3-upload")
    public ResponseEntity<ImageRes> postImageUpload(@RequestPart(value = "file") List<MultipartFile> multipartFiles, @AuthenticationPrincipal PrincipalDetails principalDetails,
                                                    @RequestParam String type) {

        // 파일 존재 여부 확인
        imageService.validateFileExists(multipartFiles);

        // 토큰에 저장된 유저 ID 꺼내는 로직
        String email = principalDetails.getUsername();

        // S3 upload
        ImageRes response = new ImageRes();
        List<String> imageList = imageService.uploadS3(multipartFiles, email, type);
        response.setImageList(imageList);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
