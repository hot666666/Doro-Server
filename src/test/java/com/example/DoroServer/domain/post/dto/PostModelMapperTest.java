package com.example.DoroServer.domain.post.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.DoroServer.domain.post.entity.Post;
import com.example.DoroServer.global.config.ModelMapperConfig;

@SpringBootTest(classes = ModelMapperConfig.class)
public class PostModelMapperTest {

    @Autowired
    private ModelMapper mapper;

    CreatePostReq setUpCreatePostReq() {
        return CreatePostReq.builder()
                .title("title")
                .content("content")
                .ownerName("ownerName")
                .phoneNumber("01000000000")
                .institution("institution")
                .email("email")
                .password("password")
                .build();
    }

    PostDto setUpPostDto() {
        return PostDto.builder()
                .id(1L)
                .title("title")
                .content("content")
                .ownerName("ownerName")
                .phoneNumber("01000000000")
                .institution("institution")
                .email("email")
                .isAnswered(false)
                .build();
    }

    PostDto setUpUpdatedPostDto() {
        return PostDto.builder()
                .id(1L)
                .title("updatedTitle")
                .content("updatedContent")
                .ownerName("updatedOwnerName")
                .phoneNumber("01000000000")
                .institution("updatedInstitution")
                .email("updatedEmail")
                .isAnswered(false)
                .build();
    }

    UpdatePostReq setUpUpdatePostReq() {
        return UpdatePostReq.builder()
                .currentPassword("password")
                .title("updatedTitle")
                .content("updatedContent")
                .ownerName("updatedOwnerName")
                .phoneNumber("01000000000")
                .institution("updatedInstitution")
                .email("updatedEmail")
                .build();
    }

    Post setUpPost() {
        return Post.builder()
                .id(1L)
                .title("title")
                .content("content")
                .ownerName("ownerName")
                .phoneNumber("01000000000")
                .institution("institution")
                .password("password")
                .email("email")
                .build();
    }

    Post setUpupdatedPost() {
        return Post.builder()
                .id(1L)
                .title("updatedTitle")
                .content("updatedContent")
                .ownerName("updatedOwnerName")
                .phoneNumber("01000000000")
                .institution("updatedInstitution")
                .password("password")
                .email("updatedEmail")
                .build();
    }

    @Test
    @DisplayName("문의글 ReqDto to Entity Mapper 테스트")
    // Post post = modelMapper.map(createPostReq, Post.class);
    void createPostReqToEntityTest() {
        // given
        CreatePostReq createPostReq = setUpCreatePostReq();
        Post post = setUpPost();

        // when
        Post result = mapper.map(createPostReq, Post.class);

        // then
        for (Field field : Post.class.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getName().equals("id")) {
                continue;
            }
            try {
                assertEquals(field.get(post), field.get(result));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    @DisplayName("문의글 Entity to Dto Mapper 테스트")
    void createPostEntityToDtoTest() {
        // given
        Post post = setUpPost();
        PostDto postDto = setUpPostDto();

        // when
        PostDto result = mapper.map(post, PostDto.class);

        // then
        for (Field field : PostDto.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                assertEquals(field.get(postDto), field.get(result));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
