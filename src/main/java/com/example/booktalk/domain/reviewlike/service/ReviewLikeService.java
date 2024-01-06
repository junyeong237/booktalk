package com.example.booktalk.domain.reviewlike.service;

import com.example.booktalk.domain.review.entity.Review;
import com.example.booktalk.domain.review.repository.ReviewRepository;
import com.example.booktalk.domain.reviewlike.dto.response.ReviewLiketoggleRes;
import com.example.booktalk.domain.reviewlike.entity.ReviewLike;
import com.example.booktalk.domain.reviewlike.repository.ReviewLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;

    public ReviewLiketoggleRes toggleReviewLike(Long reviewId, User user) {
        Review review = findReview(reviewId);

        if(Objects.equals(review.getUser().getId(), user.getId())) {
            throw new IllegalArgumentException("본인의 게시글에는 좋아요를 누를 수 없습니다.");
        }

        // 이미 게시글에 하트를 눌렀는 지 확인
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

    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 게시글이 없습니다."));
    }

}
