package com.example.booktalk.domain.review.service;

import com.example.booktalk.domain.comment.repository.CommentRepository;
import com.example.booktalk.domain.review.dto.request.ReviewCreateReq;
import com.example.booktalk.domain.review.dto.request.ReviewUpdateReq;
import com.example.booktalk.domain.review.dto.response.ReviewCreateRes;
import com.example.booktalk.domain.review.dto.response.ReviewDeleteRes;
import com.example.booktalk.domain.review.dto.response.ReviewGetListRes;
import com.example.booktalk.domain.review.dto.response.ReviewGetRes;
import com.example.booktalk.domain.review.dto.response.ReviewUpdateRes;
import com.example.booktalk.domain.review.entity.Review;
import com.example.booktalk.domain.review.exception.NotPermissionReviewAuthorityException;
import com.example.booktalk.domain.review.exception.ReviewErrorCode;
import com.example.booktalk.domain.review.repository.ReviewRepository;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;


    public ReviewCreateRes createReview(ReviewCreateReq req, Long userId) {

        User user = findUser(userId);

        Review review = Review.builder()
            .title(req.title())
            .content(req.content())
            .user(user)
            .build();

        reviewRepository.save(review);

        return new ReviewCreateRes(review);
    }

    public List<ReviewGetListRes> getReviewList(String sortBy, boolean isAsc) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        List<Review> reviewList = reviewRepository.findAll(sort);

        return reviewList.stream().map(
            ReviewGetListRes::new
        ).toList();
    }

    public ReviewGetRes getReview(Long reviewId) {

        Review review = reviewRepository.findReviewByIdWithThrow(reviewId);

        return new ReviewGetRes(review);
    }

    @Transactional
    public ReviewUpdateRes updateReview(Long reviewId, ReviewUpdateReq req, Long userId) {

        User user = findUser(userId);
        Review review = reviewRepository.findReviewByIdWithThrow(reviewId);
        validateReviewUser(user, review);

        review.update(req);

        return new ReviewUpdateRes(review);
    }

    public ReviewDeleteRes deleteReview(Long reviewId, Long userId) {

        User user = findUser(userId);
        Review review = reviewRepository.findReviewByIdWithThrow(reviewId);
        validateReviewUser(user, review);

        reviewRepository.delete(review);

        return ReviewDeleteRes.builder()
            .msg("리뷰 게시글이 삭제되었습니다.")
            .build();
    }


    private User findUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다."));
    }


    private void validateReviewUser(User user, Review review) {
        if (!user.getId().equals(review.getUser().getId())) {
            throw new NotPermissionReviewAuthorityException(
                ReviewErrorCode.NOT_PERMISSION_REVIEW_AUTHORITY);
        }
    }

}
