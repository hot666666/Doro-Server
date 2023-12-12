package com.example.DoroServer.domain.post.api;

import com.example.DoroServer.domain.post.dto.CreatePostReq;
import com.example.DoroServer.domain.post.dto.PasswordReq;
import com.example.DoroServer.domain.post.dto.PostDto;
import com.example.DoroServer.domain.post.dto.UpdatePostReq;
import com.example.DoroServer.domain.post.service.PostService;
import com.example.DoroServer.global.common.SuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "문의글 📋")
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostApi {

    private final PostService postService;

    // 문의글 생성
    @PostMapping
    @ApiOperation(value = "문의글 생성", notes = "문의글 생성")
    public SuccessResponse<?> createPost(@RequestBody @Valid CreatePostReq createPostReq) {
        PostDto postId = postService.createPost(createPostReq);
        return SuccessResponse.successResponse(postId);
    }

    // 문의글 수정
    @PatchMapping("/{id}")
    @ApiOperation(value = "문의글 수정", notes = "바꾸고 싶은 필드만 넣어서 update해도 되고 값 다 넣어서 해도 됩니다. 게시물의 주인인지 확인하기 위해 currentpassowrd 에 유저가 입력했던 비밀번호를 넣어줘야 합니다.")
    public SuccessResponse<?> updatePost(
            @PathVariable("id") Long postId,
            @RequestBody @Valid UpdatePostReq updatePostReq) {
        PostDto updatedPost = postService.updatePost(postId, updatePostReq);

        return SuccessResponse.successResponse(updatedPost);
    }

    // 문의글 조회
    @GetMapping("/{id}")
    @ApiOperation(value = "문의글 조회", notes = "문의글 조회")
    public SuccessResponse<?> findPost(
            @PathVariable("id") Long postId,
            @RequestBody PasswordReq password) {

        PostDto findPostRes = postService.findPost(postId, password.getPassword());

        return SuccessResponse.successResponse(findPostRes);
    }

    // 문의글 삭제
    @DeleteMapping("/{id}")
    @ApiOperation(value = "문의글 삭제", notes = "문의글 삭제")
    public SuccessResponse<?> deletePost(
            @PathVariable("id") Long postId,
            @RequestBody PasswordReq password) {
        postService.deletePost(postId, password.getPassword());
        return SuccessResponse.successResponse("삭제 완료");
    }

}
