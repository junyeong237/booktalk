package com.example.booktalk.domain.review.service;

import com.example.booktalk.domain.review.dto.request.ReviewCreateReq;
import com.example.booktalk.domain.review.dto.request.ReviewUpdateReq;
import com.example.booktalk.domain.review.dto.response.*;
import com.example.booktalk.domain.review.entity.Review;
import com.example.booktalk.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // 리뷰 게시글 생성
    public ReviewCreateRes createReview(ReviewCreateReq req) {
        Review review = Review.builder()
                .title(req.title())
                .content(req.content())
                .build();

        reviewRepository.save(review);

        return ReviewCreateRes.builder()
                .reviewId(review.getId())
                .title(review.getTitle())
                .content(review.getContent())
                .build();
    }

    // 리뷰 게시글 리스트 조회
    public List<ReviewGetListRes> getReviewList() {
        List<Review> reviewList = reviewRepository.findAll();

        return reviewList.stream().map(review -> ReviewGetListRes.builder()
                .reviewId(review.getId())
                .title(review.getTitle())
                .build()).toList();
    }

    // 리뷰 게시글 단건 조회
    public ReviewGetRes getReview(Long reviewId) {
        Review review = findReview(reviewId);

        return ReviewGetRes.builder()
                .reviewId(review.getId())
                .title(review.getTitle())
                .content(review.getContent())
                .build();
    }

    // 리뷰 게시글 수정
    @Transactional
    public ReviewUpdateRes updateReview(Long reviewId, ReviewUpdateReq req) {
        Review review = findReview(reviewId);

        review.update(req);

        return ReviewUpdateRes.builder()
                .title(review.getTitle())
                .content(review.getContent())
                .build();
    }

    // 리뷰 게시글 삭제
    public ReviewDeleteRes deleteReview(Long reviewId) {
        Review review = findReview(reviewId);

        reviewRepository.delete(review);

        return ReviewDeleteRes.builder()
                .msg("삭제되었습니다.")
                .build();
    }


    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 게시글이 없습니다."));
    }



}
