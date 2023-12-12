package com.example.DoroServer.domain.post.api;

import com.example.DoroServer.domain.post.dto.AnswerReq;
import com.example.DoroServer.domain.post.dto.FindAllPostRes;
import com.example.DoroServer.domain.post.dto.PostDto;
import com.example.DoroServer.domain.post.service.PostAdminService;
import com.example.DoroServer.global.common.SuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

@Api(tags = "문의글 관리 📋")
@RestController
@RequestMapping("/posts-admin")
@RequiredArgsConstructor
public class PostAdminApi {

    private final PostAdminService postAdminService;

    // 답변 작성
    @Secured("ROLE_ADMIN")
    @PatchMapping("/{id}/answer")
    @ApiOperation(value = "문의글 답변 업데이트", notes = "관리자만 가능, body에 json으로 {answer: value} 이런식으로 넣어주면됩니다")
    public SuccessResponse<?> updateAnswer(
            @PathVariable("id") Long postId,
            @RequestBody AnswerReq answerReq) {
        PostDto updatedPost = postAdminService.updateAnswer(postId, answerReq.getAnswer());

        return SuccessResponse.successResponse(updatedPost);
    }

    // 문의글 조회
    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}")
    @ApiOperation(value = "문의글 조회", notes = "관리자만 가능, 문의글 조회")
    public SuccessResponse<?> findPost(@PathVariable("id") Long postId) {

        PostDto foundPost = postAdminService.findPost(postId);

        return SuccessResponse.successResponse(foundPost);
    }

    // ===================================
    // 문의글 전부 조회(get all paging 사용)
    @Secured("ROLE_ADMIN")
    @GetMapping
    @ApiOperation(value = "문의글 전체 조회", notes = "관리자만 가능, 문의글 전체 조회 page 기본 사이즈 10,기본 page 0입니다. page,size 파라미터로 조절 가능")
    public SuccessResponse<?> findAllPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        FindAllPostRes findAllPostRes = postAdminService.findAllPost(pageRequest);
        return SuccessResponse.successResponse(findAllPostRes);
    }

    // 답변없는 문의글 조회(get all paging 사용)
    @Secured("ROLE_ADMIN")
    @GetMapping("/not-answered")
    @ApiOperation(value = "답변없는 문의글 전체 조회", notes = "관리자만 가능, 문의글 전체 조회 page 기본 사이즈 10,기본 page 0입니다. page,size 파라미터로 조절 가능")
    public SuccessResponse<?> findAllPostsNotAnswered(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        FindAllPostRes findAllPostRes = postAdminService.findAllPostsNotAnswered(pageRequest);
        return SuccessResponse.successResponse(findAllPostRes);
    }
    // ===================================

    // 문의글 삭제
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    @ApiOperation(value = "문의글 삭제", notes = "관리자만 가능, 문의글 삭제")
    public SuccessResponse<?> deletePost(@PathVariable("id") Long postId) {

        postAdminService.deletePost(postId);

        return SuccessResponse.successResponse("삭제 완료");
    }

}
