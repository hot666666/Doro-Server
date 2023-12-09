package com.example.DoroServer.domain.lectureContentImage.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.DoroServer.domain.lectureContent.entity.LectureContent;
import com.example.DoroServer.domain.lectureContent.repository.LectureContentRepository;
import com.example.DoroServer.domain.lectureContentImage.dto.LectureContentImageMapper;
import com.example.DoroServer.domain.lectureContentImage.dto.LectureContentImageReq;
import com.example.DoroServer.domain.lectureContentImage.dto.LectureContentImageRes;
import com.example.DoroServer.domain.lectureContentImage.entity.LectureContentImage;
import com.example.DoroServer.domain.lectureContentImage.repository.LectureContentImageRepository;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import com.example.DoroServer.global.s3.AwsS3ServiceImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LectureContentImageService {
    private final AwsS3ServiceImpl awsS3Service;
    private final LectureContentImageRepository lectureContentImageRepository;
    private final LectureContentRepository lectureContentRepository;
    private final LectureContentImageMapper lectureContentImageMapper;

    public List<LectureContentImageRes> addLectureContentImages(Long id,
            LectureContentImageReq lectureContentImageReq) {
        LectureContent lectureContent = findLectureContentById(id);

        validateDtoFiles(lectureContentImageReq.getFiles());

        List<String> uploadedUrls = uploadFilesAndGetUrls(lectureContentImageReq.getFiles());

        List<LectureContentImage> lectureContentImages = createLectureContentImagesWith(uploadedUrls);
        lectureContentImages = lectureContentImageRepository.saveAll(lectureContentImages);
        lectureContent.getImages().addAll(lectureContentImages);
        return lectureContentImageMapper.toLectureContentImageResList(lectureContentImages);
    }

    public void deleteLectureContentImage(Long contentId, Long imageId) {
        LectureContent lectureContent = findLectureContentById(contentId);
        LectureContentImage lectureContentImage = getLectureContentImage(imageId, lectureContent);

        deleteS3Image(lectureContentImage);
        lectureContent.getImages().remove(lectureContentImage);
        lectureContentImageRepository.delete(lectureContentImage);
    }

    /* 서비스 코드에서 사용되는 메서드 */

    void validateDtoFiles(MultipartFile[] files) {
        // MultipartFile이 한 개라도 배열에 넣어서 요청해야함
        if (files == null || files.length == 0) {
            throw new BaseException(Code.JSON_SYNTAX_ERROR);
        }
        if (files.length > 100) {
            throw new BaseException(Code.LECTURE_CONTENT_IMAGE_COUNT_OVER);
        }
        for (MultipartFile file : files) {
            if (file.getSize() > 10485760) { // 10MB
                throw new BaseException(Code.LECTURE_CONTENT_IMAGE_SIZE_OVER);
            }
        }
    }

    private LectureContentImage getLectureContentImage(Long imageId, LectureContent lectureContent) {
        LectureContentImage lectureContentImage = lectureContent.getImages().stream()
                .filter(image -> image.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new BaseException(Code.LECTURE_CONTENT_IMAGE_NOT_FOUND));
        return lectureContentImage;
    }

    LectureContent findLectureContentById(Long id) {
        return lectureContentRepository.findById(id)
                .orElseThrow(() -> new BaseException(Code.LECTURE_CONTENT_NOT_FOUND));
    }

    List<LectureContentImage> createLectureContentImagesWith(List<String> uploadedUrls) {
        // 업로드된 파일들의 URL로 LectureContentImage를 만들어 리스트로 반환
        return uploadedUrls.stream()
                .map(url -> LectureContentImage.builder().url(url).build())
                .collect(Collectors.toList());
    }

    List<String> uploadFilesAndGetUrls(MultipartFile[] files) {
        // S3 BUCKET/lecture-content에 파일 업로드 후, 업로드된 파일들의 URL 반환
        return Arrays.stream(files)
                .map(file -> awsS3Service.upload(file, "lecture-content"))
                .collect(Collectors.toList());
    }

    void deleteS3Image(LectureContentImage lectureContentImage) {
        String uploadedUrl = lectureContentImage.getUrl();
        String fileName = getFileNameFrom(uploadedUrl);
        awsS3Service.deleteImage(fileName);
    }

    String getFileNameFrom(String url) {
        // UUID(36자) + .jpg(4자) = 파일명(40자)
        return url.substring(40);
    }

}
