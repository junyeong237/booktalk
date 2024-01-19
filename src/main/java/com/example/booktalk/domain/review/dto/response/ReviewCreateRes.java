package com.example.booktalk.domain.review.dto.response;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ReviewCreateRes(
    Long reviewId,
    String title,
    String content,
    String nickname,
    String getReviewImagePathUrl
) {
}
