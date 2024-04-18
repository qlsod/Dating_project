package com.example.dating.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.dating.repository.ProfileImagesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final AmazonS3 amazonS3;
    private final ProfileImagesRepository profileImagesRepository;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.object}")
    private String object;

    public void validateFileExists(List<MultipartFile> multipartFiles) {
        if (multipartFiles.isEmpty() || multipartFiles.size() > 6) {
            throw new RuntimeException("파일이 존재하지 않습니다");
        }
    }

    public List<String> uploadS3(List<MultipartFile> multipartFiles, String email, String type) {

//        if (multipartFiles.size() != 1) {
        ArrayList<String> urlList = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {

            // 파일 이름 지정
            String keyName = "cammet/" + type + "/" + email + "/" + file.getOriginalFilename();

            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentType(file.getContentType());
            objMeta.setContentLength(file.getSize());

            try {
                amazonS3.putObject(bucket, keyName, file.getInputStream(), objMeta);
            } catch (IOException e) {
                throw new RuntimeException("업로드 실패");
            }

            // 업로드 된 이미지 URL 받기
            String imageURL = object + keyName;

            urlList.add(imageURL);

        }

        log.info("imageURL:{}", urlList);

        return urlList;
        }
    }


