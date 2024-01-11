package com.example.booktalk.domain.review.dto.response;

public record ReviewUpdateRes(
    Long id,
    String title,
    String content
) {
}
