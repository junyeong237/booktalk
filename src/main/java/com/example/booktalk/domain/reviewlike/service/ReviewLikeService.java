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

@Service
@RequiredArgsConstructor
public class ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public ReviewLiketoggleRes toggleReviewLike(Long reviewId, Long userId) {
        User user = findUser(userId);
        Review review = findReview(reviewId);

        validateReviewLikeUser(user, review);

        ReviewLike existReviewLike = reviewLikeRepository.findByReviewAndUser(review, user);

        if(existReviewLike != null) {
            reviewLikeRepository.delete(existReviewLike);
            review.decreaseReviewLike();
            return ReviewLiketoggleRes.builder()
                    .msg("좋아요 취소")
                    .build();
        }

        ReviewLike reviewLike = new ReviewLike(user, review);
        reviewLikeRepository.save(reviewLike);
        review.increaseReviewLike();
        return ReviewLiketoggleRes.builder()
                .msg("좋아요!")
                .build();

    }


    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다."));
    }

    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundReviewException(ReviewErrorCode.NOT_FOUND_REVIEW));
    }

    private void validateReviewLikeUser(User user, Review review) {
        if(user.getId().equals(review.getUser().getId())) {
            throw new NotPermissionToggleException(ReviewLikeErrorCode.NOT_PERMISSION_TOGGLE);
        }
    }

}
