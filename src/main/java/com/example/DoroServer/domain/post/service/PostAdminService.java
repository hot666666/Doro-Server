package com.example.DoroServer.domain.post.service;

import com.example.DoroServer.domain.post.dto.*;
import com.example.DoroServer.domain.post.entity.Post;
import com.example.DoroServer.domain.post.repository.PostRepository;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostAdminService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public PostDto updateAnswer(Long postId, String answer) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new BaseException(Code.BAD_REQUEST));

        post.enrollAnswer(answer);
        postRepository.save(post);

        return modelMapper.map(post, PostDto.class);

    }

    public FindAllPostRes findAllPost(Pageable pageable) {

        Page<Post> posts = postRepository.findAll(pageable);
        FindAllPostRes findAllPostRes = toFindAllPostRes(posts);

        return findAllPostRes;
    }

    public FindAllPostRes findAllPostsNotAnswered(Pageable pageable) {

        Page<Post> posts = postRepository.findByIsAnsweredFalse(pageable);
        FindAllPostRes findAllPostRes = toFindAllPostRes(posts);

        return findAllPostRes;
    }

    public PostDto findPost(Long id) {

        Post post = postRepository.findById(id).orElseThrow(() -> new BaseException(Code.BAD_REQUEST));

        return modelMapper.map(post, PostDto.class);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    FindAllPostRes toFindAllPostRes(Page<Post> posts) {
        List<PostDto> findAllPostResList = posts.stream().map(post -> modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());

        FindAllPostRes findAllPostRes = FindAllPostRes.builder()
                .total(posts.getTotalElements())
                .posts(findAllPostResList)
                .build();

        return findAllPostRes;
    }

}
