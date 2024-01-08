package com.example.booktalk.domain.reviewlike.repository;

import com.example.booktalk.domain.review.entity.Review;
import com.example.booktalk.domain.reviewlike.entity.ReviewLike;
import com.example.booktalk.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    ReviewLike findByReviewAndUser(Review review, User user);
}
