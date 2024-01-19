package com.example.booktalk.domain.review.controller;

import com.example.booktalk.domain.review.dto.request.ReviewCreateReq;
import com.example.booktalk.domain.review.dto.request.ReviewUpdateReq;
import com.example.booktalk.domain.review.dto.response.*;
import com.example.booktalk.domain.review.service.ReviewService;
import com.example.booktalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewCreateRes> createReview(
            @RequestBody ReviewCreateReq req,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(req, userDetails.getUser().getId()));
    }

    @GetMapping
    public ResponseEntity<List<ReviewGetListRes>> getReviewList(
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewList(sortBy, isAsc));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewGetRes> getReview(
            @PathVariable(name = "reviewId") Long reviewId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReview(reviewId));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewUpdateRes> updateReview(
            @PathVariable(name = "reviewId") Long reviewId,
            @RequestBody ReviewUpdateReq req,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.updateReview(reviewId, req, userDetails.getUser().getId()));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ReviewDeleteRes> deleteReview(
            @PathVariable(name = "reviewId") Long reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.deleteReview(reviewId, userDetails.getUser().getId()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReviewSearchListRes>> getReviewSearchList(
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc,
            @RequestParam(value = "query") String search
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewSearchList(sortBy, isAsc, search));
    }



}
