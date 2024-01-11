package com.example.booktalk.domain.reviewlike.service;

import com.example.booktalk.domain.review.entity.Review;
import com.example.booktalk.domain.review.exception.NotFoundReviewException;
import com.example.booktalk.domain.review.exception.ReviewErrorCode;
import com.example.booktalk.domain.review.repository.ReviewRepository;
import com.example.booktalk.domain.reviewlike.dto.response.ReviewLiketoggleRes;
import com.example.booktalk.domain.reviewlike.entity.ReviewLike;
import com.example.booktalk.domain.reviewlike.exception.NotPermissionToggleException;
import com.example.booktalk.domain.reviewlike.exception.ReviewLikeErrorCode;
import com.example.booktalk.domain.reviewlike.repository.ReviewLikeRepository;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewLiketoggleRes toggleReviewLike(Long reviewId, Long userId) {

        User user = userRepository.findUserByIdWithThrow(userId);
        Review review = reviewRepository.findReviewByIdWithThrow(reviewId);
        validateReviewLikeUser(user, review);

        Optional<ReviewLike> existReviewLike = reviewLikeRepository.findByReviewAndUser(review, user);

        if(existReviewLike.isPresent()) {
            reviewLikeRepository.delete(existReviewLike.get());
            review.decreaseReviewLike();
            return new ReviewLiketoggleRes("좋아요 취소");
        }

        ReviewLike reviewLike = ReviewLike.builder()
                .user(user)
                .review(review)
                .build();
        reviewLikeRepository.save(reviewLike);
        review.increaseReviewLike();
        return new ReviewLiketoggleRes("좋아요!");

    }

    private void validateReviewLikeUser(User user, Review review) {
        if(user.getId().equals(review.getUser().getId())) {
            throw new NotPermissionToggleException(ReviewLikeErrorCode.NOT_PERMISSION_TOGGLE);
        }
    }

}
