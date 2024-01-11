package com.example.booktalk.domain.review.dto.response;

import com.example.booktalk.domain.review.entity.Review;

public record ReviewUpdateRes(
    Long id,

    String title,
    String content
) {

    public ReviewUpdateRes(Review review) {
        this(review.getId(), review.getTitle(), review.getContent());
    }
}
