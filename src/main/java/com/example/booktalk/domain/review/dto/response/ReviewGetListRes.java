package com.example.booktalk.domain.review.dto.response;

import com.example.booktalk.domain.review.entity.Review;

public record ReviewGetListRes(
    Long reviewId,
    String title,

    String name
) {

    public ReviewGetListRes(Review review) {
        this(review.getId(), review.getTitle(), review.getUser().getNickname());
    }
}
