package com.example.booktalk.domain.review.service;

import com.example.booktalk.domain.comment.dto.response.CommentGetListRes;
import com.example.booktalk.domain.comment.repository.CommentRepository;
import com.example.booktalk.domain.review.dto.request.ReviewCreateReq;
import com.example.booktalk.domain.review.dto.request.ReviewUpdateReq;
import com.example.booktalk.domain.review.dto.response.*;
import com.example.booktalk.domain.review.entity.Review;
import com.example.booktalk.domain.review.exception.NotPermissionReviewAuthorityException;
import com.example.booktalk.domain.review.exception.ReviewErrorCode;
import com.example.booktalk.domain.review.repository.ReviewRepository;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.entity.UserRoleType;
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
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;


    public ReviewCreateRes createReview(ReviewCreateReq req, Long userId) {

        User user = userRepository.findUserByIdWithThrow(userId);

        Review review = Review.builder()
                .title(req.title())
                .content(req.content())
                .user(user)
                .build();

        Review result = reviewRepository.save(review);

        return new ReviewCreateRes(result.getId(), result.getTitle(),
                result.getContent(), result.getUser().getNickname());
    }

    public List<ReviewGetListRes> getReviewList(String sortBy, boolean isAsc) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        List<Review> reviewList = reviewRepository.findAll(sort);

        return reviewList.stream()
                .map(review -> new ReviewGetListRes(review.getId(),
                        review.getTitle(), review.getUser().getNickname(),
                        review.getReviewLikeCount()))
                .toList();
    }

    public List<ReviewSearchListRes> getReviewSearchList(String sortBy, boolean isAsc, String search) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        List<Review> reviewList = reviewRepository.getReviewListByTitleOrContent(sort, search);

        return reviewList.stream()
                .map(review -> new ReviewSearchListRes(review.getId(),
                        review.getTitle(), review.getUser().getNickname(),
                        review.getReviewLikeCount()))
                .toList();
    }

    public ReviewGetRes getReview(Long reviewId) {

        Review review = reviewRepository.findReviewByIdWithThrow(reviewId);

        List<CommentGetListRes> commentList = commentRepository.findAllByReviewOrderByCreatedAtDesc(review)
                .stream().map(comment -> new CommentGetListRes(comment.getId(),
                        comment.getContent(),comment.getUser().getNickname()))
                .toList();

        return new ReviewGetRes(review.getId(), review.getTitle(),
                review.getContent(), review.getUser().getNickname(),
                review.getReviewLikeCount(), review.getCreatedAt(), review.getModifiedAt(), commentList);
    }

    @Transactional
    public ReviewUpdateRes updateReview(Long reviewId, ReviewUpdateReq req, Long userId) {

        User user = userRepository.findUserByIdWithThrow(userId);
        Review review = reviewRepository.findReviewByIdWithThrow(reviewId);
        validateReviewUser(user, review);

        review.update(req);

        return new ReviewUpdateRes(review.getId(), review.getTitle(), review.getContent());
    }

    public ReviewDeleteRes deleteReview(Long reviewId, Long userId) {

        User user = userRepository.findUserByIdWithThrow(userId);
        Review review = reviewRepository.findReviewByIdWithThrow(reviewId);
        validateReviewUser(user, review);

        reviewRepository.delete(review);

        return new ReviewDeleteRes("리뷰 게시글이 삭제되었습니다.");

    }

    private void validateReviewUser(User user, Review review) {
        if (!user.getId().equals(review.getUser().getId())
                && !user.getRole().equals(UserRoleType.ADMIN) ) {
            throw new NotPermissionReviewAuthorityException(
                    ReviewErrorCode.NOT_PERMISSION_REVIEW_AUTHORITY);
        }
    }


}
