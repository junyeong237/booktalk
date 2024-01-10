package com.example.booktalk.domain.review.entity;

import jakarta.persistence.PrePersist;

public class ReviewListener {

    @PrePersist
    public void prePersist(Review review) {
        if(review.getReviewLikeCount() == null) {
            review.setReviewLikeCount(0);
        }
    }
}
