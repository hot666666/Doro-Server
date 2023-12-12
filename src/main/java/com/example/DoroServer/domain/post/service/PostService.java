package com.example.DoroServer.domain.post.service;

import com.example.DoroServer.domain.post.dto.*;
import com.example.DoroServer.domain.post.entity.Post;
import com.example.DoroServer.domain.post.repository.PostRepository;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public PostDto createPost(CreatePostReq createPostReq) {
        Post post = modelMapper.map(createPostReq, Post.class);

        Post savedPost = postRepository.save(post);

        return modelMapper.map(savedPost, PostDto.class);
    }

    // 답변, 게시글 수정에 모두 사용됩니다.(model mapper 에서 null 값은 업데이트를 안치게 적용)
    public PostDto updatePost(Long postId, UpdatePostReq updatePostReq) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BaseException(Code.BAD_REQUEST));

        // 작성자의 비밀번호가 일치하지 않습니다.
        if (!post.getPassword().equals(updatePostReq.getCurrentPassword())) {
            throw new BaseException(Code.BAD_REQUEST);
        }
        // model mapper 적용
        modelMapper.map(updatePostReq, post);
        postRepository.save(post);

        return modelMapper.map(post, PostDto.class);
    }

    public PostDto findPost(Long id, String password) {
        Post post = postRepository.findById(id).orElseThrow(() -> new BaseException(Code.BAD_REQUEST));

        // 작성자의 비밀번호가 일치하지 않습니다.
        if (!post.getPassword().equals(password)) {
            throw new BaseException(Code.BAD_REQUEST);
        }

        return modelMapper.map(post, PostDto.class);
    }

    public void deletePost(Long id, String password) {
        Post post = postRepository.findById(id).orElseThrow(() -> new BaseException(Code.BAD_REQUEST));

        // 작성자의 비밀번호가 일치하지 않습니다.
        if (!post.getPassword().equals(password)) {
            throw new BaseException(Code.BAD_REQUEST);
        }

        postRepository.delete(post);
    }

    boolean checkPassword(Long id, String password) {
        Post post = postRepository.findById(id).orElseThrow(() -> new BaseException(Code.USER_NOT_FOUND));

        if (!post.getPassword().equals(password)) {
            return false;
        }
        return true;
    }

}
