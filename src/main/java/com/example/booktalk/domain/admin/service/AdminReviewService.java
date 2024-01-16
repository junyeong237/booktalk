package com.example.booktalk.domain.admin.service;

import com.example.booktalk.domain.review.dto.response.ReviewDeleteRes;
import com.example.booktalk.domain.review.entity.Review;
import com.example.booktalk.domain.review.repository.ReviewRepository;
import com.example.booktalk.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminReviewService {
    private final ReviewRepository reviewRepository;
    public ReviewDeleteRes adminDeleteReview(Long reviewId) {
        Review review = reviewRepository.findReviewByIdWithThrow(reviewId);

        reviewRepository.delete(review);

        return new ReviewDeleteRes("리뷰 게시글이 삭제되었습니다.");
    }
}
