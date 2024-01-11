package com.example.booktalk.domain.review.dto.response;

import com.example.booktalk.domain.review.entity.Review;

public record ReviewCreateRes(
    Long reviewId,
    String title,
    String content,
    String nickname
) {

    public ReviewCreateRes(Review review) {
        this(review.getId(), review.getTitle(), review.getContent(),
            review.getUser().getNickname());
    }


}
