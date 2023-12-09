package com.example.DoroServer.domain.lectureContent.service;

import com.example.DoroServer.domain.lectureContent.dto.LectureContentMapper;
import com.example.DoroServer.domain.lectureContent.dto.CreateLectureContentReq;
import com.example.DoroServer.domain.lectureContent.dto.LectureContentRes;
import com.example.DoroServer.domain.lectureContent.dto.UpdateLectureContentReq;
import com.example.DoroServer.domain.lectureContent.entity.LectureContent;
import com.example.DoroServer.domain.lectureContent.repository.LectureContentRepository;
import com.example.DoroServer.domain.lectureContentImage.entity.LectureContentImage;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import com.example.DoroServer.global.s3.AwsS3ServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LectureContentService {

    private final AwsS3ServiceImpl awsS3Service;
    private final LectureContentRepository lectureContentRepository;
    private final LectureContentMapper lectureContentMapper;
    private final ModelMapper modelMapper;

    /* 서비스 코드 */

    public List<LectureContentRes> findAllLectureContents() {
        List<LectureContent> lectureContentList = lectureContentRepository.findAll();

        return lectureContentMapper.toLectureContentResList(lectureContentList);
    }

    public LectureContentRes createLectureContent(CreateLectureContentReq lectureContentReq) {
        validateDtoFiles(lectureContentReq.getFiles());

        LectureContent lectureContent = modelMapper.map(lectureContentReq, LectureContent.class);

        List<String> uploadedUrls = uploadFilesAndGetUrls(lectureContentReq.getFiles());

        List<LectureContentImage> lectureContentImages = createLectureContentImagesWith(uploadedUrls);
        lectureContent.getImages().addAll(lectureContentImages);
        LectureContent savedLectureContent = lectureContentRepository.save(lectureContent);

        return lectureContentMapper.toLectureContentRes(savedLectureContent);
    }

    public LectureContentRes updateLectureContent(Long id, UpdateLectureContentReq updateLectureContentReq) {
        LectureContent lectureContent = findLectureContentById(id);

        modelMapper.map(updateLectureContentReq, lectureContent); // 필드있고없고차이 MapStruct와 비교

        LectureContent updatedLectureContent = lectureContentRepository.save(lectureContent);

        return lectureContentMapper.toLectureContentRes(updatedLectureContent);
    }

    public void deleteLectureContent(Long id) {
        LectureContent lectureContent = findLectureContentById(id);

        lectureContent.getImages().forEach(this::deleteS3Image);

        lectureContentRepository.delete(lectureContent);
    }

    /* 서비스 코드에서 사용되는 메서드 */

    void validateDtoFiles(MultipartFile[] files) {
        if (files == null) {
            // Multipart가 한 개라도 배열에 넣어서 요청해야함
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
