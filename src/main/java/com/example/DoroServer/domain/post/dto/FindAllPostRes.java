package com.example.DoroServer.domain.post.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
public class FindAllPostRes {
    private Long total;

    private List<PostDto> posts;

}
