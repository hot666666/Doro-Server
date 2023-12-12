package com.example.DoroServer.domain.post.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.DoroServer.domain.post.dto.CreatePostReq;
import com.example.DoroServer.domain.post.dto.PostDto;
import com.example.DoroServer.domain.post.dto.UpdatePostReq;
import com.example.DoroServer.domain.post.entity.Post;
import com.example.DoroServer.domain.post.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ModelMapper modelMapper;

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
    @DisplayName("게시글 생성 테스트")
    void createPostTest() {
        // given
        CreatePostReq createPostReq = setUpCreatePostReq();
        Post post = setUpPost();
        PostDto postDto = setUpPostDto();

        given(modelMapper.map(createPostReq, Post.class)).willReturn(post);
        given(postRepository.save(any(Post.class))).willReturn(post);
        given(modelMapper.map(post, PostDto.class)).willReturn(postDto);

        // when
        postService.createPost(createPostReq);

        // then
        verify(modelMapper, times(1)).map(createPostReq, Post.class);
        verify(postRepository, times(1)).save(any(Post.class));
        verify(modelMapper, times(1)).map(post, PostDto.class);
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    void updatePostTest() {
        // given
        UpdatePostReq updatePostReq = setUpUpdatePostReq();
        Post post = setUpPost();
        Post setUpupdatedPost = setUpupdatedPost();
        PostDto postDto = setUpUpdatedPostDto();

        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(post));
        doNothing().when(modelMapper).map(any(UpdatePostReq.class), any(Post.class));
        given(postRepository.save(any(Post.class))).willReturn(setUpupdatedPost);
        given(modelMapper.map(any(Post.class), eq(PostDto.class))).willReturn(postDto);

        // when
        postService.updatePost(1L, updatePostReq);

        // then
        verify(postRepository, times(1)).findById(any(Long.class));
        verify(modelMapper, times(1)).map(any(UpdatePostReq.class), any(Post.class));
        verify(postRepository, times(1)).save(any(Post.class));
        verify(modelMapper, times(1)).map(any(Post.class), eq(PostDto.class));
    }

    @Test
    @DisplayName("게시글 조회 테스트")
    void findPostTest() {
        // given
        Post post = setUpPost();
        PostDto postDto = setUpPostDto();

        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(post));
        given(modelMapper.map(any(Post.class), eq(PostDto.class))).willReturn(postDto);

        // when
        postService.findPost(1L, "password");

        // then
        verify(postRepository, times(1)).findById(any(Long.class));
        verify(modelMapper, times(1)).map(any(Post.class), eq(PostDto.class));
    }

    @Test
    @DisplayName("게시글 조회 실패 테스트 - 다른 비밀번호")
    void findPostFailTest() {
        // given
        Post post = setUpPost();

        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(post));

        // when & then
        assertThatThrownBy(() -> {
            postService.findPost(1L, "wrongPassword");
        }).isInstanceOf(Exception.class);
    }
}
