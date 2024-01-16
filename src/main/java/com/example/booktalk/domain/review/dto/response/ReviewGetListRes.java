package com.example.booktalk.domain.review.dto.response;

public record ReviewGetListRes(
    Long reviewId,
    String title,
    String name,
    Integer reviewLike
) {
}
