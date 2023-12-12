package com.example.DoroServer.domain.post.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PostDto {
    private Long id; // PK

    private String title;

    private String content;

    private String ownerName;

    private String institution;

    private String phoneNumber;

    private String email;

    private String answer;

    private boolean isAnswered;

}
