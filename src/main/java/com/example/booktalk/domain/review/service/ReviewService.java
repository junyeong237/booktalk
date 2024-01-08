package com.example.booktalk.domain.review.service;

import com.example.booktalk.domain.review.dto.request.ReviewCreateReq;
import com.example.booktalk.domain.review.dto.request.ReviewUpdateReq;
import com.example.booktalk.domain.review.dto.response.*;
import com.example.booktalk.domain.review.entity.Review;
import com.example.booktalk.domain.review.exception.NotFoundReviewException;
import com.example.booktalk.domain.review.exception.NotPermissionReviewAuthorityException;
import com.example.booktalk.domain.review.exception.ReviewErrorCode;
import com.example.booktalk.domain.review.repository.ReviewRepository;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public ReviewCreateRes createReview(ReviewCreateReq req, Long userId) {

        User user = findUser(userId);

        Review review = Review.builder()
                .title(req.title())
                .content(req.content())
                .user(user)
                .build();

        reviewRepository.save(review);

        return ReviewCreateRes.builder()
                .reviewId(review.getId())
                .title(review.getTitle())
                .content(review.getContent())
                .user(review.getUser())
                .build();
    }

    public List<ReviewGetListRes> getReviewList(String sortBy, boolean isAsc) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        List<Review> reviewList = reviewRepository.findAll(sort);

        return reviewList.stream().map(review -> ReviewGetListRes.builder()
                .reviewId(review.getId())
                .title(review.getTitle())
                .build()).toList();
    }

    public ReviewGetRes getReview(Long reviewId) {

        Review review = findReview(reviewId);

        return ReviewGetRes.builder()
                .reviewId(review.getId())
                .title(review.getTitle())
                .content(review.getContent())
                .user(review.getUser())
                .commentList(review.getCommentList())
                .build();
    }

    @Transactional
    public ReviewUpdateRes updateReview(Long reviewId, ReviewUpdateReq req, Long userId) {

        User user = findUser(userId);
        Review review = findReview(reviewId);
        validateReviewUser(user, review);

        review.update(req);

        return ReviewUpdateRes.builder()
                .title(review.getTitle())
                .content(review.getContent())
                .build();
    }

    public ReviewDeleteRes deleteReview(Long reviewId, Long userId) {

        User user = findUser(userId);
        Review review = findReview(reviewId);
        validateReviewUser(user, review);

        reviewRepository.delete(review);

        return ReviewDeleteRes.builder()
                .msg("삭제되었습니다.")
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

    private void validateReviewUser(User user, Review review) {
        if(!user.getId().equals(review.getUser().getId())) {
            throw new NotPermissionReviewAuthorityException(ReviewErrorCode.NOT_PERMISSION_REVIEW_AUTHORITY);
        }
    }

}
