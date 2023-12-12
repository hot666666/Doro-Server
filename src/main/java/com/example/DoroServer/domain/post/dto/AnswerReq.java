package com.example.DoroServer.domain.post.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AnswerReq {
    @NotBlank(message = "답변을 입력해주세요")
    private String answer;

}
