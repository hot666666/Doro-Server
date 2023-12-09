package com.example.DoroServer.domain.lectureContentImage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class LectureContentImageRes {
    private Long id; // PK
    private String url; // uploaded image url
}
