package com.example.booktalk.domain.review.controller;

import com.example.booktalk.domain.review.dto.request.ReviewUpdateReq;
import com.example.booktalk.domain.review.dto.response.*;
import com.example.booktalk.domain.review.dto.request.ReviewCreateReq;
import com.example.booktalk.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewCreateRes> createReview(@RequestBody ReviewCreateReq req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(req));
    }

    @GetMapping
    public ResponseEntity<List<ReviewGetListRes>> getReviewList() {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewList());
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewGetRes> getReview(@PathVariable Long reviewId) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReview(reviewId));
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<ReviewUpdateRes> updateReview(@PathVariable Long reviewId, @RequestBody ReviewUpdateReq req) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.updateReview(reviewId, req));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ReviewDeleteRes> deleteReview(@PathVariable Long reviewId) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.deleteReview(reviewId));
    }



}
