package com.example.booktalk.domain.review.dto.response;

public record ByMeReviewGetListRes(
    Long reviewId,
    String title,
    String name,
    Integer reviewLike
) {
}
